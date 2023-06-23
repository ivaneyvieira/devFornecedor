package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class PedidoCompraFornecedor(
    val vendno: Int,
    val fornecedor: String,
    val cnpj: String,
    val dataPedidoInicio: LocalDate,
    val dataPedidoFim: LocalDate,
    val vlPedido: Double,
    val vlCancelado: Double,
    val vlRecebido: Double,
    val vlPendente: Double,
    val pedidos: List<PedidoCompra>,
) {

  val dataPedidoInicioStr
    get() = dataPedidoInicio.format()
  val dataPedidoFimStr
    get() = dataPedidoFim.format()

  companion object {
    fun findAll(filtro: FiltroPedidoCompra): List<PedidoCompraFornecedor> {
      val list = PedidoCompra.findAll(filtro)
      return group(list)
    }

    fun group(list: List<PedidoCompra>) = list.groupBy { it.vendno }.mapNotNull { entry ->
      val pedidos = entry.value
      val bean = pedidos.firstOrNull() ?: return@mapNotNull null
      PedidoCompraFornecedor(
          vendno = bean.vendno,
          fornecedor = bean.fornecedor,
          cnpj = bean.cnpj,
          dataPedidoInicio = pedidos.minOf { it.dataPedido },
          dataPedidoFim = pedidos.maxOf { it.dataPedido },
          vlPedido = pedidos.sumOf { it.vlPedido },
          vlCancelado = pedidos.sumOf { it.vlCancelado },
          vlRecebido = pedidos.sumOf { it.vlRecebido },
          vlPendente = pedidos.sumOf { it.vlPendente },
          pedidos = pedidos,
      )
    }
  }
}