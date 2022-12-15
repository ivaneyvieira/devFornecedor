package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.pdftxt.DataLine
import br.com.astrosoft.devolucao.model.pdftxt.FileText
import br.com.astrosoft.devolucao.model.pdftxt.Line
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail
import io.github.rushuat.ocell.document.Document

class TabConferirViewModel(val viewModel: CompraViewModel) : ITabCompraConfViewModel {
  private var fileText: FileText? = null
  private var dataLine: DataLine? = null

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

  override fun findLineByProduto(produto: PedidoCompraProduto): Line? {
    val dl = dataLine ?: return null
    val list = produto.listCodigo()
    return list.firstNotNullOfOrNull { dl.find(it) }
  }

  override fun pedidoOK(): Boolean {
    return fileText != null
  }

  override fun savePdfPedido(pedido: PedidoCompra, bytes: ByteArray) {
    pedido.savePdf(bytes)
  }

  override fun removePedido(pedido: PedidoCompra) {
    pedido.removePdf()
    setFileText(null)
  }

  override fun confirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>) = viewModel.exec {
    if (itens.isEmpty()) {
      fail("Newnhum item selecionado")
    }
    itens.forEach { item ->
      item.marcaConferido()
    }
  }

  override fun setFileText(fileText: FileText?) {
    this.fileText = fileText
    this.dataLine = fileText?.listLinesDados()
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
            .sortedWith(compareBy({ it.vendno }, { it.loja }, { it.dataPedido }, { it.linha }, {it.codigo}))
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