package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class PedidosCompraProduto(
  val vendno: Int,
  val fornecedor: String,
  val cnpj: String,
  val loja: Int,
  val numeroPedido: Int,
  val status: Int,
  val dataPedido: LocalDate,
  val dataEntrega: LocalDate,
  val obsercacaoPedido: String,
  val codigo: String,
  val descricao: String,
  val refFab: String,
  val grade: String,
  val unidade: String,
  val qtPedida: Int,
  val qtCancelada: Int,
  val qtRecebida: Int,
  val qtPendente: Int,
  val custoUnit: Double,
  val barcode: String,
                          ) {
  companion object {
    fun findAll(filtro: FiltroPedidoCompra): List<PedidosCompraProduto> = saci.findPedidosCompraProduto(filtro)
  }
}

data class FiltroPedidoCompra(val loja: Int, val pesquisa: String)