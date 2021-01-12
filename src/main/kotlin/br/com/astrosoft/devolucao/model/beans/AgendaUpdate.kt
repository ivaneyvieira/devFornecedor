package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class AgendaUpdate(val invno: Int, var data: LocalDate?, var hora: String?,
                   var recebedor: String?) {
  fun save() {
    saci.updateAgenda(this)
  }
  
  val funcionario
    get() = saci.listFuncionario(recebedor ?: "")
}