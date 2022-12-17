package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail
import io.github.rushuat.ocell.document.Document

class TabConferirViewModel(val viewModel: CompraViewModel) : ITabCompraConfViewModel {
  private val listPedidoExcel = mutableListOf<PedidoExcel>()

  val subView
    get() = viewModel.view.tabConferirViewModel

  fun updateComponent() = viewModel.exec {
    val list = pedidoCompraFornecedors()
    subView.updateGrid(list)
  }

  private fun pedidoCompraFornecedors(): List<PedidoCompraFornecedor> {
    val filtro = subView.filtro()
    val list = PedidoCompraFornecedor.findAll(filtro).filter {
      (!it.fornecedor.startsWith("ENGECOPI"))
    }

    return list
  }

  fun listLojas(): List<Loja> {
    val list = Loja.allLojas() + Loja.lojaZero
    return list.sortedBy { it.no }
  }

  override fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>) = viewModel.exec {
    pedido.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimirRelatorioFornecedor(pedido)
  }

  override fun findPedidoExcel(produto: PedidoCompraProduto): PedidoExcel? {
    val list = produto.listCodigo()
    return listPedidoExcel.firstOrNull { list.contains(it.referencia)}
  }

  override fun pedidoOK(): Boolean {
    return listPedidoExcel.isNotEmpty()
  }

  override fun saveExcelPedido(pedido: PedidoCompra, bytes: ByteArray) {
    pedido.saveExcel(bytes)
  }

  override fun removeExcelPedido(pedido: PedidoCompra) {
    pedido.removeExcel()
    setFileExcel(null)
  }

  override fun confirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>) = viewModel.exec {
    if (itens.isEmpty()) {
      fail("Newnhum item selecionado")
    }
    itens.forEach { item ->
      item.marcaConferido()
    }
  }

  override fun setFileExcel(fileText: ByteArray?) {
    if (fileText == null) {
      listPedidoExcel.clear()
    }
    else {
      Document().use { document ->
        document.fromBytes(fileText)
        val list = document.getSheet(PedidoExcel::class.java)
        listPedidoExcel.clear()
        listPedidoExcel.addAll(list)
        listPedidoExcel.forEachIndexed { index, pedidoExcel ->
          pedidoExcel.linha = index + 1
        }
      }
    }
    updateComponent()
  }

  fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Nenhuma fornecedor foi selecionada")
    }
    subView.imprimirRelatorioResumido(fornecedores)
  }

  override fun imprimirPedidoCompra(pedidos: List<PedidoCompra>) = viewModel.exec {
    pedidos.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimeSelecionados(pedidos)
  }

  override fun excelPedidoCompra(pedidos: List<PedidoCompra>): ByteArray {
    return if (pedidos.isEmpty()) {
      viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    }
    else {
      Document().use { document ->
        val listaPedidos =
          pedidos
            .flatMap { it.produtos }
            .sortedWith(compareBy({ it.vendno }, { it.loja }, { it.dataPedido }, { it.linha }, { it.codigo }))
        document.addSheet(listaPedidos)
        document.toBytes()
      }
    }
  }

  fun excelRelatorioFornecedor(pedidos: List<PedidoCompra>): ByteArray {
    return if (pedidos.isEmpty()) {
      viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    }
    else Document().use { document ->
      document.addSheet(pedidos.sortedWith(compareBy({ it.vendno }, { it.loja }, { it.dataPedido })))
      document.toBytes()
    }
  }
}

interface ITabConferirViewModel : ITabView {
  fun filtro(): FiltroPedidoCompra
  fun updateGrid(itens: List<PedidoCompraFornecedor>)
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
  fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>)
  fun imprimeSelecionados(pedidos: List<PedidoCompra>)
}