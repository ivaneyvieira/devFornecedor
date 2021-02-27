package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class Agenda(
  val loja: Int,
  val data: LocalDate?,
  val hora: String,
  val empno: Int,
  val recebedor: String,
  val dataRecbedor: LocalDate?,
  val horaRecebedor: String,
  val invno: String,
  val fornecedor: Int,
  val abreviacao: String,
  val emissao: LocalDate?,
  val nf: String,
  val volume: String,
  val total: Double,
  val transp: Int,
  val nome: String,
  val pedido: Int
            ) {
  val dataHoraRecebimento
    get() = "${dataRecbedor.format()} $horaRecebedor".trim()

  fun agendaUpdate() = AgendaUpdate(
    invno.toIntOrNull() ?: 0,
    data,
    hora,
    if (empno == 0) "" else empno.toString(),
    dataRecbedor,
    horaRecebedor
                                   )

  companion object {
    fun listaAgenda(agendado: Boolean, recebido: Boolean, filtro: String, loja: Int) =
      saci.listaAgenda(agendado, recebido, filtro, loja)
  }
}