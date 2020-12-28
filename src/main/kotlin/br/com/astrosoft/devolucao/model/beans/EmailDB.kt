package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
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
  }
}