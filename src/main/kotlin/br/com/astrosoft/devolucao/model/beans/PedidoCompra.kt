package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class PedidoCompra(
  val vendno: Int,
  val fornecedor: String,
  val cnpj: String,
  val loja: Int,
  val numeroPedido: Int,
  val status: Int,
  val dataPedido: LocalDate,
  val dataEntrega: LocalDate,
  val obsercacaoPedido: String,
  val vlPedida: Double,
  val vlCancelada: Double,
  val vlRecebida: Double,
  val vlPendente: Double,
  val produtos: List<PedidosCompraProduto>,
                  ) {
  companion object {
    fun findAll(filtro: FiltroPedidoCompra): List<PedidoCompra> = PedidosCompraProduto.findAll(filtro).groupBy {
      ChavePedidoCompra(loja = it.loja, numeroPedido = it.numeroPedido)
    }.mapNotNull { entry ->
      val produtos = entry.value
      val bean = produtos.firstOrNull() ?: return@mapNotNull null
      PedidoCompra(
        vendno = bean.vendno,
        fornecedor = bean.fornecedor,
        cnpj = bean.cnpj,
        loja = bean.loja,
        numeroPedido = bean.numeroPedido,
        status = bean.status,
        dataPedido = bean.dataPedido,
        dataEntrega = bean.dataEntrega,
        obsercacaoPedido = bean.obsercacaoPedido,
        vlPedida = produtos.sumOf { it.qtPedida * it.custoUnit },
        vlCancelada = produtos.sumOf { it.qtCancelada * it.custoUnit },
        vlRecebida = produtos.sumOf { it.qtRecebida * it.custoUnit },
        vlPendente = produtos.sumOf { it.qtPendente * it.custoUnit },
        produtos = produtos,
                  )
    }
  }
}

data class ChavePedidoCompra(val loja: Int, val numeroPedido: Int)