package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.framework.util.toDate
import java.time.LocalDate

class ProdutosNotaSaida(
  val loja: Int,
  val pdv: Int,
  val transacao: Int,
  val codigo: String,
  val refFor: String,
  val descricao: String,
  val grade: String,
  val qtde: Int,
  val barcode: String,
  val un: String,
  val st: String,
  val valorUnitario: Double,
  val valorTotal: Double,
  val invno: Int,
  val vendno: Int,
  val rotulo: String,
  val quantInv: Int,
  val notaInv: String,
  val dateInv: LocalDate?,
  val valorUnitInv: Double,
  val valorTotalInv: Double,
  val ipi: Double,
  val vst: Double,
  val valorTotalIpi: Double,
  val chaveUlt: String?,
  val cst: String,
  val cfop: String,
                       ) {
  var item: Int = 0
  val dateInvDate
    get() = dateInv?.toDate()
  var nota: NotaSaida? = null
}