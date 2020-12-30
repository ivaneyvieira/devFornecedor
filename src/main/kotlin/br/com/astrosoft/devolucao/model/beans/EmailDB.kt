package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.MailGMail
import java.time.LocalDate
import java.time.LocalTime

class EmailDB(val storeno: Int,
              val pdvno: Int,
              val xano: Int,
              val data: LocalDate,
              val hora: LocalTime,
              val idEmail: Int,
              val email: String,
              val assunto: String,
              val msg: String,
              val planilha: String,
              val relatorio: String,
              val anexos: String) {
  fun notasEmail() = saci.listNotasEmailNota(idEmail)
  fun emailBean() = EmailGmail(
    email = email,
    assunto = assunto,
    msg = msg,
    msgHtml = "",
    planilha = planilha,
    relatorio = relatorio,
    anexos = anexos
                              )
  
  fun isEmailEnviado() = idEmail > 0
  fun isEmailRecebido() = !isEmailEnviado()
  
  val tipoEmail
    get() = if(isEmailRecebido()) "Recebido" else "Enviado"
  
  companion object {
    fun newEmailId() = saci.newEmailId()
    
    fun listEmailRecebidos(): List<EmailDB> {
      val gmail = MailGMail()
      val emails = gmail.listEmail("")
      return emails.map {msg ->
        EmailDB(
          storeno = 0,
          pdvno = 0,
          xano = 0,
          data = msg.data.toLocalDate(),
          hora = msg.data.toLocalTime(),
          idEmail = 0,
          email = msg.from.toString(),
          assunto = msg.subject,
          msg = msg.content.messageTxt,
          planilha = "N",
          relatorio = "N",
          anexos = "N"
               )
      }
    }
  }
}