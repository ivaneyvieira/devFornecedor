package br.com.astrosoft.framework.model

import java.io.UnsupportedEncodingException
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class MailGMail {
  val emailRemetente = "eng.alerta@gmail.com"
  val nomeRemetente = "Engecopi"
  val protocolo = "smtp"
  val servidor = "smtp.gmail.com" // do painel de controle do SMTP
  val username = "engecopi.devolucao@gmail.com" // do painel de controle do SMTP
  val senha = "devfor04" // do painel de controle do SMTP
  val porta = "465" // do painel de controle do SMTP
  val props = initProperties()
  val session: Session =
    Session.getDefaultInstance(props, GmailAuthenticator(username, senha))
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
    val transport: Transport = session.getTransport(protocolo)
    transport.connect(servidor, username, senha)
    message.saveChanges()
    transport.sendMessage(message, message.allRecipients)
    transport.close()
  }
  
  private fun initProperties(): Properties {
    return Properties().apply {
      this["mail.transport.protocol"] = protocolo
      this["mail.smtp.host"] = servidor
      this["mail.smtp.auth"] = "true"
      this["mail.smtp.port"] = porta
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
    val message = MimeMessage(session)
    //message.replyTo = iaReplyTo
    message.setFrom(iaFrom)
    if(iaTo.isNotEmpty()) message.setRecipients(Message.RecipientType.TO, iaTo)
    message.subject = subject
    message.sentDate = Date()
    return message
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