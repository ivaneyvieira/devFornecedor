package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.beans.PedidoExcel

interface ITabCompraViewModel {
  fun imprimirPedidoCompra(pedidos: List<PedidoCompra>)
  fun excelPedidoCompra(pedidos: List<PedidoCompra>): ByteArray
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
}

interface  ITabCompraConfViewModel:ITabCompraViewModel{
  fun saveExcelPedido(pedido: PedidoCompra, bytes: ByteArray)
  fun removeExcelPedido(pedido: PedidoCompra)
  fun confirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>)
  fun pedidoOK(): Boolean
  fun setFileExcel(fileText: ByteArray?)
  fun findPedidoExcel(produto: PedidoCompraProduto): PedidoExcel?
}