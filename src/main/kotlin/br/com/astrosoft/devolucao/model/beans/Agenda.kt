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
  val conhecimento: String,
  val dataRecbedor: LocalDate?,
  val horaRecebedor: String,
  val invno: String,
  val fornecedor: Int,
  val abreviacao: String,
  val cnpj: String?,
  val emissao: LocalDate?,
  val nf: String,
  val volume: String,
  val total: Double,
  val transp: Int,
  val nome: String,
  val pedido: Int,
  val frete: String?,
  val coleta: LocalDate?,
) {
  val dataHoraRecebimento
    get() = "${dataRecbedor.format()} $horaRecebedor".trim()

  fun agendaUpdate() = AgendaUpdate(
    invno = invno.toIntOrNull() ?: 0,
    coleta = coleta,
    data = data,
    hora = hora,
    recebedor = if (empno == 0) "" else empno.toString(),
    conhecimento = conhecimento,
    dataRecbedor = dataRecbedor,
    horaRecebedor = horaRecebedor,
  )

  companion object {
    fun listaAgenda(agendado: Boolean, recebido: Boolean, filtro: String, loja: Int) =
        saci.listaAgenda(agendado, recebido, filtro, loja)
  }
}