package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.GamilFolder.Recebidos
import br.com.astrosoft.framework.model.MailGMail
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.mail.internet.InternetAddress

class EmailDB(
  val storeno: Int,
  val pdvno: Int,
  val xano: Int,
  val data: LocalDate,
  val hora: LocalTime,
  val idEmail: Int,
  val messageID: String,
  val email: String,
  val assunto: String,
  val msg: String,
  val planilha: String,
  val relatorio: String,
  val anexos: String
) {
  fun notasEmail() = saci.listNotasEmailNota(idEmail)
  fun emailBean() = EmailGmail(
    email = email,
    assunto = assunto,
    msg = { msg },
    msgHtml = "",
    planilha = planilha,
    relatorio = relatorio,
    relatorioResumido = "N",
    anexos = anexos,
    messageID = messageID
  )

  fun isEmailEnviado() = idEmail != 0
  fun isEmailRecebido() = idEmail == 0

  fun dataHora(): LocalDateTime? = LocalDateTime.of(data, hora)

  val tipoEmail
    get() = if (isEmailRecebido()) "Recebido" else "Enviado"

  companion object {
    fun newEmailId() = saci.newEmailId()

    fun listEmailRecebidos(): List<EmailDB> {
      val gmail = MailGMail()
      val emails = gmail.listEmail(Recebidos, "")
      val emailsEnviados = saci.listEmailPara()
      return emails.mapNotNull { msgRecebido ->
        val from = (msgRecebido.from.getOrNull(0) as? InternetAddress)?.address ?: ""
        val emailResposta = emailsEnviados.filter { emailEnviado ->
          emailEnviado.email.contains(from) && emailEnviado.dataHora()?.isAfter(msgRecebido.data) == true
        }
        if (emailResposta.toList().isNotEmpty()) null
        else EmailDB(
          storeno = 0,
          pdvno = 0,
          xano = 0,
          data = msgRecebido.data.toLocalDate(),
          hora = msgRecebido.data.toLocalTime(),
          idEmail = 0,
          messageID = msgRecebido.messageID,
          email = from,
          assunto = msgRecebido.subject,
          msg = msgRecebido.content().messageTxt,
          planilha = "N",
          relatorio = "N",
          anexos = "N"
        )
      }
    }
  }
}