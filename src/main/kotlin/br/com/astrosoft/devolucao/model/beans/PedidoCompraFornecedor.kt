package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class PedidoCompraFornecedor(
  val vendno: Int,
  val fornecedor: String,
  val cnpj: String,
  val dataPedido: LocalDate,
  val vlPedido: Double,
  val vlCancelado: Double,
  val vlRecebido: Double,
  val vlPendente: Double,
  val pedidos: List<PedidoCompra>,
                            ) {
  companion object {
    fun findAll(filtro: FiltroPedidoCompra) = PedidoCompra.findAll(filtro).groupBy { it.vendno }.mapNotNull { entry ->
      val pedidos = entry.value
      val bean = pedidos.firstOrNull() ?: return@mapNotNull null
      PedidoCompraFornecedor(
        vendno = bean.vendno,
        fornecedor = bean.fornecedor,
        cnpj = bean.cnpj,
        dataPedido = pedidos.maxOf { it.dataPedido },
        vlPedido = pedidos.sumOf { it.vlPedido },
        vlCancelado = pedidos.sumOf { it.vlCancelado },
        vlRecebido = pedidos.sumOf { it.vlRecebido },
        vlPendente = pedidos.sumOf { it.vlPendente },
        pedidos = pedidos,
                            )
    }
  }
}