package br.com.astrosoft.framework.model

import com.sun.mail.imap.IMAPFolder
import java.io.UnsupportedEncodingException
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Store
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class MailGMail {
  val emailRemetente = "engecopi.devolucao@gmail.com"
  val nomeRemetente = "Engecopi"
  val protocolo = "smtp"
  val servidorSmtp = "smtp.gmail.com" // do painel de controle do SMTP
  val username = "engecopi.devolucao@gmail.com" // do painel de controle do SMTP
  val senha = "devfor04" // do painel de controle do SMTP
  val portaSmtp = "465" // do painel de controle do SMTP
  val propsSmtp = initPropertiesSmtp()
  val sessionSmtp: Session =
    Session.getDefaultInstance(propsSmtp, GmailAuthenticator(username, senha))
      .apply {
        debug = false
      }
  
  fun sendMail(to: String, subject: String, htmlMessage: String, files: List<FileAttach> = emptyList()): Boolean {
    try {
      val message = createMessage(to, subject)
      val multPart = MimeMultipart().apply {
        val partText = partText(htmlMessage)
        val partFile = partsFile(files)
        this.addBodyPart(partText)
        partFile.forEach {part ->
          this.addBodyPart(part)
        }
      }
      message.setContent(multPart)
      transport(message)
      return true
    } catch(e: UnsupportedEncodingException) {
      e.printStackTrace()
      return false
    } catch(e: MessagingException) {
      e.printStackTrace()
      return false
    }
  }
  
  private fun transport(message: MimeMessage) {
    val transport: Transport = sessionSmtp.getTransport(protocolo)
    transport.connect(servidorSmtp, username, senha)
    message.saveChanges()
    transport.sendMessage(message, message.allRecipients)
    transport.close()
  }
  
  private fun initPropertiesSmtp(): Properties {
    return Properties().apply {
      this["mail.transport.protocol"] = protocolo
      this["mail.smtp.host"] = servidorSmtp
      this["mail.smtp.auth"] = "true"
      this["mail.smtp.port"] = portaSmtp
      this["mail.smtp.ssl.enable"] = "true";
    }
  }
  
  private fun partsFile(files: List<FileAttach>): List<MimeBodyPart> {
    return files.map {file ->
      MimeBodyPart().apply {
        val fileDataSource = FileDataSource(file.fileName())
        this.dataHandler = DataHandler(fileDataSource)
        this.fileName = fileDataSource.name
      }
    }
  }
  
  private fun partText(htmlMessage: String): MimeBodyPart {
    return MimeBodyPart().apply {
      this.dataHandler = DataHandler((HTMLDataSource(htmlMessage)))
    }
  }
  
  private fun createMessage(toList: String, subject: String): MimeMessage {
    val toSplit =
      toList.split(",")
        .toList()
        .map {it.trim()}
    val iaFrom = InternetAddress(emailRemetente, nomeRemetente)
    val iaTo = arrayOfNulls<InternetAddress>(toSplit.size)
    //val iaReplyTo = arrayOfNulls<InternetAddress>(1)
    // iaReplyTo[0] = InternetAddress(to, to)
    toSplit.forEachIndexed {index, to ->
      iaTo[index] = InternetAddress(to, to)
    }
    val message = MimeMessage(sessionSmtp)
    //message.replyTo = iaReplyTo
    message.setFrom(iaFrom)
    if(iaTo.isNotEmpty()) message.setRecipients(Message.RecipientType.TO, iaTo)
    message.subject = subject
    message.sentDate = Date()
    return message
  }
  
  fun listEmail() {
    var folder: IMAPFolder? = null
    var store: Store? = null
  
    try {
      val props = System.getProperties()
      props.setProperty("mail.store.protocol", "imaps")
      val session = Session.getDefaultInstance(props,  GmailAuthenticator(username, senha))
      store = session.getStore("imaps")
      store.connect("imap.googlemail.com", username, senha)
  
      store.defaultFolder.list().forEach {
        println(it.name)
      }
      
      folder = store.getFolder("INBOX") as IMAPFolder? // This doesn't work for other email account
      //folder = (IMAPFolder) store.getFolder("inbox"); This works for both email account
      if(!folder!!.isOpen) folder.open(Folder.READ_WRITE)
      val messages = folder.messages
      println("No of Messages : " + folder.messageCount)
      println("No of Unread Messages : " + folder.unreadMessageCount)
      println(messages.size)
      for(i in messages.indices) {
        println("*****************************************************************************")
        println("MESSAGE " + (i + 1) + ":")
        val msg = messages[i]
        //System.out.println(msg.getMessageNumber());
        //Object String;
        //System.out.println(folder.getUID(msg)
        val subject = msg.subject
        println("Subject: $subject")
        println("From: " + msg.from[0])
        println("To: " + msg.allRecipients[0])
        println("Date: " + msg.receivedDate)
        println("Size: " + msg.size)
        println(msg.flags)
        println("""
  Body:
  ${msg.content}
  """.trimIndent())
        val rep = msg
        println(msg.contentType)
      }
    } finally {
      if(folder != null && folder.isOpen) {
        folder.close(true)
      }
      store?.close()
    }
  }
}

class GmailAuthenticator(val username: String, val password: String): Authenticator() {
  override fun getPasswordAuthentication(): PasswordAuthentication {
    return PasswordAuthentication(username, password)
  }
}
/*
fun main() {
  var message = "<i>Greetings!</i><br>"
  message += "<b>Wish you a nice day!</b><br>"
  message += "<font color=red>Duke</font>"
  val mail = MailGMail();
  mail.sendMail(DestinoMail("Ivaney", "ivaneyvieira@gmail.com"), "Teste 01", message)
}
*
 */


fun main() {
  val mail = MailGMail()
  mail.listEmail()
}