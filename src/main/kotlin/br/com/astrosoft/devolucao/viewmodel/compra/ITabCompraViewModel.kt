package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra

interface ITabCompraViewModel {
  fun imprimirPedidoCompra(pedidos: List<PedidoCompra>)
  fun excelPedidoCompra(pedidos: List<PedidoCompra>): ByteArray
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
}