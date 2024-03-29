package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.pdftxt.FileText

interface ITabCompraViewModel {
  fun imprimirPedidoCompra(pedidos: List<PedidoCompra>)
  fun excelPedidoCompra(pedidos: List<PedidoCompra>): ByteArray
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
  fun updateComponent()
  fun listPedidosFornecedor(): List<PedidoCompraFornecedor>
}

interface ITabCompraConfViewModel : ITabCompraViewModel {
  fun saveExcelPedido(pedido: PedidoCompra, bytes: ByteArray)
  fun removeExcelPedido(pedido: PedidoCompra)
  fun savePDFPedido(pedido: PedidoCompra, bytes: ByteArray)
  fun removePDFPedido(pedido: PedidoCompra)
  fun confirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>)
  fun usaEmbalagemProdutoSelecionado(itens: Set<PedidoCompraProduto>)
  fun desconfirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>)
  fun pedidoOK(): EFileType
  fun setFileExcel(fileText: ByteArray?)
  fun setFilePDF(fileText: ByteArray?)
  fun setPedidoExcel(produto: PedidoCompraProduto)
  fun setPedidoPDF(produto: PedidoCompraProduto)

  fun tipoPainel(): ETipoPainel

  fun fileText(): FileText

  fun ajustaSaldoEmbalagem(itens: Set<PedidoCompraProduto>)

  override fun updateComponent()
}

enum class EFileType {
  NONE, PDF, XLSX
}

enum class ETipoPainel {
  Conferir, Conferido
}