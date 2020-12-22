package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate
import java.time.LocalTime

class EmailEnviado(val storeno: Int,
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
  fun emailBean() = EmailBean(
    email = email,
    assunto = assunto,
    msg = msg,
    msgHtml = "",
    planilha = planilha,
    relatorio = relatorio,
    anexos = anexos
                             )
  
  companion object {
    fun newEmailId() = saci.newEmailId()
  }
}