package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class PedidoCompra(
  val vendno: Int,
  val fornecedor: String,
  val cnpj: String,
  val loja: Int,
  val sigla: String,
  val numeroPedido: Int,
  val status: Int,
  val dataPedido: LocalDate,
  val dataEntrega: LocalDate,
  val obsercacaoPedido: String,
  val vlPedido: Double,
  val vlCancelado: Double,
  val vlRecebido: Double,
  val vlPendente: Double,
  val produtos: List<PedidoCompraProduto>,
                  ) {
  val labelTitle
    get() = "FORNECEDOR: ${this.vendno} ${this.fornecedor}                  LOJA: $sigla PEDIDO: $numeroPedido"

  val labelGroup
    get() = "FORNECEDOR: ${this.vendno} ${this.fornecedor}"

  val dataPedidoStr
    get() = dataPedido.format()

  companion object {
    fun findAll(filtro: FiltroPedidoCompra): List<PedidoCompra> {
      val list = PedidoCompraProduto.findAll(filtro)
      return group(list)
    }

    fun group(list: List<PedidoCompraProduto>): List<PedidoCompra> {
      return list.groupBy {
        ChavePedidoCompra(loja = it.loja, numeroPedido = it.numeroPedido)
      }.mapNotNull { entry ->
        val produtos = entry.value
        val bean = produtos.firstOrNull() ?: return@mapNotNull null
        PedidoCompra(
          vendno = bean.vendno,
          fornecedor = bean.fornecedor,
          cnpj = bean.cnpj,
          loja = bean.loja,
          sigla = bean.sigla,
          numeroPedido = bean.numeroPedido,
          status = bean.status,
          dataPedido = bean.dataPedido,
          dataEntrega = bean.dataEntrega,
          obsercacaoPedido = bean.obsercacaoPedido,
          vlPedido = produtos.sumOf { it.vlPedido },
          vlCancelado = produtos.sumOf { it.vlCancelado },
          vlRecebido = produtos.sumOf { it.vlRecebido },
          vlPendente = produtos.sumOf { it.vlPendente },
          produtos = produtos,
                    )
      }
    }
  }
}

data class ChavePedidoCompra(val loja: Int, val numeroPedido: Int)