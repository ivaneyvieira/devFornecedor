package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.pdftxt.FileText
import br.com.astrosoft.devolucao.model.pdftxt.Line

interface ITabCompraViewModel {
  fun imprimirPedidoCompra(pedidos: List<PedidoCompra>)
  fun excelPedidoCompra(pedidos: List<PedidoCompra>): ByteArray
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
}

interface  ITabCompraConfViewModel:ITabCompraViewModel{
  fun savePdfPedido(pedido: PedidoCompra, bytes: ByteArray)
  fun removePedido(pedido: PedidoCompra)
  fun confirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>)
  fun pedidoOK(): Boolean
  fun setFileText(fileText: FileText?)
  fun findLineByProduto(produto: PedidoCompraProduto): Line?
}