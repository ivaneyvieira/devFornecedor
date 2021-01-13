package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class Agenda(val loja: Int, val data: LocalDate?, val hora: String, val empno: Int, val recebedor: String,
             val invno: String, val fornecedor: Int, val abreviacao: String, val emissao: LocalDate?, val nf: String,
             val volume: String, val total: Double, val transp: Int, val nome: String, val pedido: Int) {
  fun agendaUpdate() = AgendaUpdate(invno.toIntOrNull() ?: 0, data, hora, if(empno == 0) "" else empno.toString())
  
  companion object {
    fun listaAgenda(agendado: Boolean, recebido: Boolean) = saci.listaAgenda(agendado, recebido)
  }
}