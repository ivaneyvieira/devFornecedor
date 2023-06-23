package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

data class AgendaUpdate(
    val invno: Int,
    var coleta: LocalDate?,
    var data: LocalDate?,
    var hora: String?,
    var recebedor: String?,
    var conhecimento: String?,
    val dataRecbedor: LocalDate?,
    val horaRecebedor: String?,
) {
  fun save() {
    saci.updateAgenda(this)
  }

  val funcionario
    get() = saci.listFuncionario(recebedor ?: "")
}