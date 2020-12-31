package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.toDate
import br.com.astrosoft.framework.util.toLocalDate
import br.com.astrosoft.framework.util.toLocalDateTime
import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage
import com.sun.mail.imap.IMAPStore
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Address
import javax.mail.Authenticator
import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Store
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.search.AndTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SubjectTerm
import javax.mail.FetchProfile
import javax.mail.FetchProfile.Item

class MailGMail {
  private val emailRemetente = "engecopi.devolucao@gmail.com"
  private val nomeRemetente = "Engecopi"
  private val protocolo = "smtp"
  private val servidorSmtp = "smtp.gmail.com" // do painel de controle do SMTP
  private val username = "engecopi.devolucao@gmail.com" // do painel de controle do SMTP
  private val senha = "devfor04" // do painel de controle do SMTP
  private val portaSmtp = "465" // do painel de controle do SMTP
  private val propsSmtp = initPropertiesSmtp()
  private val sessionSmtp: Session =
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
      this["mail.smtp.ssl.enable"] = "true"
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
      this.setText(htmlMessage)
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
  
  fun listEmail(subjectSearch: String): List<EmailMessage> {
    var folder: IMAPFolder? = null
    var store: Store? = null
    
    return try {
      val props = System.getProperties()
      props.setProperty("mail.store.protocol", "imaps")
      val session = Session.getDefaultInstance(props, GmailAuthenticator(username, senha))
      store = session.getStore("imaps") as IMAPStore
      store.connect("imap.googlemail.com", username, senha)
      
      folder = store.getFolder(folderRecebidos) as IMAPFolder?
      if(folder == null) emptyList()
      else {
        if(!folder.isOpen) folder.open(Folder.READ_ONLY)
        /*
        val term =
          if(subjectSearch == "") ReceivedDateTerm(ComparisonTerm.GE,
                                                   LocalDate.of(2020, 12, 30)
                                                     .toDate())
          else AndTerm(arrayOf(SubjectTerm(subjectSearch)))
        val messages = folder.search(term)*/
        val dataInicial = LocalDate.of(2020, 12, 29)
        val messages = folder.messages
        val profile = FetchProfile()
        profile.add(Item.ENVELOPE)
        profile.add(IMAPFolder.FetchProfileItem.HEADERS)
        folder.fetch(messages, profile)
        messages.filter {
          if(subjectSearch == "")
            it.receivedDate.toLocalDate()?.isAfter(dataInicial) == true
          else it.subject.contains(subjectSearch)
        }
          .mapNotNull {message ->
            
            EmailMessage(
              messageID = (message as? IMAPMessage)?.messageID ?: "",
              subject = message.subject ?: "",
              data = message.receivedDate.toLocalDateTime() ?: LocalDateTime.now(),
              from = message.from.toList(),
              to = message.allRecipients.toList()
                        )
          }
      }
    } finally {
      if(folder != null && folder.isOpen) {
        folder.close(true)
      }
      store?.close()
    }
  }
  
  fun listMessageContent(messageID: String): List<Content> {
    var folder: IMAPFolder? = null
    var store: Store? = null
    
    return try {
      val props = System.getProperties()
      props.setProperty("mail.store.protocol", "imaps")
      val session = Session.getDefaultInstance(props, GmailAuthenticator(username, senha))
      store = session.getStore("imaps") as IMAPStore
      store.connect("imap.googlemail.com", username, senha)
      
      folder = store.getFolder(folderRecebidos) as IMAPFolder?
      if(folder == null) emptyList()
      else {
        if(!folder.isOpen) folder.open(Folder.READ_ONLY)

        val dataInicial = LocalDate.of(2020, 12, 29)
        val messages = folder.messages
        val profile = FetchProfile()
        profile.add(Item.ENVELOPE)
        profile.add(IMAPFolder.FetchProfileItem.HEADERS)
        folder.fetch(messages, profile)
        messages.filter {
          val id = (it as? IMAPMessage)?.messageID ?: ""
          id == messageID
        }.map {
          it.contentBean()
        }
      }
    } finally {
      if(folder != null && folder.isOpen) {
        folder.close(true)
      }
      store?.close()
    }
  }
  
  companion object {
    const val folderEnviados = "[Gmail]/E-mails enviados"
    const val folderRecebidos = "INBOX"
    const val folderAll = "[Gmail]/Todos os e-mails"
  }
}

private fun Message.contentBean(): Content {
  val contentLocal = content
  return if(contentLocal is Multipart) {
    val multipart = contentLocal
    val anexos = mutableListOf<Attachment>()
    var messageTxt = ""
    for(i in 0 until multipart.count) {
      if(messageTxt == "") {
        val bodyPart = multipart.getBodyPart(i)
        val disposition = bodyPart.disposition
        if(disposition?.toUpperCase() != "ATTACHMENT") messageTxt = bodyPart.content.toString()
      }
    }
    Content(messageTxt, anexos)
  }
  else Content(content.toString(), emptyList())
}

class GmailAuthenticator(val username: String, val password: String): Authenticator() {
  override fun getPasswordAuthentication(): PasswordAuthentication {
    return PasswordAuthentication(username, password)
  }
}

data class EmailMessage(
  val messageID: String,
  val subject: String,
  val data: LocalDateTime,
  val from: List<Address>,
  val to: List<Address>
                       ){
  fun content(): Content {
    val gmail = MailGMail()
    return gmail.listMessageContent(messageID).firstOrNull() ?: Content("", emptyList())
  }
}

class Attachment(
  val name: String,
  val content: ByteArray
                )

data class Content(
  val messageTxt: String,
  val anexos: List<Attachment>
                  )
/*
fun main() {
  var message = "<i>Greetings!</i><br>"
  message += "<b>Wish you a nice day!</b><br>"
  message += "<font color=red>Duke</font>"
  val mail = MailGMail();
  mail.sendMail(DestinoMail("Ivaney", "ivaneyvieira@gmail.com"), "Teste 01", message)
}
 */
/*
fun main() {
  val mail = MailGMail()
  val list = mail.listEmail("[Gmail]/Todos os e-mails", "454724")
  println("Tamanho da lista: ${list.size}")
  list.forEach {
    println("****************************************************************************************")
    println(it)
  }
}
 */

class FileAttach(val nome: String, val bytes: ByteArray) {
  fun fileName(): String {
    val fileName = "/tmp/$nome"
    val file = File(fileName)
    file.writeBytes(bytes)
    return fileName
  }
}