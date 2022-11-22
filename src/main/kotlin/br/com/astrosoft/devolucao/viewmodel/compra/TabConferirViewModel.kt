package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.pdftxt.FileText
import br.com.astrosoft.devolucao.model.pdftxt.Line
import br.com.astrosoft.devolucao.model.reports.RelatorioPedidoCompra
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail
import io.github.rushuat.ocell.document.Document

class TabConferirViewModel(val viewModel: CompraViewModel) : ITabCompraViewModel {
  private var fileText: FileText? = null
  val subView
    get() = viewModel.view.tabConferirViewModel

  fun updateComponent() = viewModel.exec {
    val list = pedidoCompraFornecedors()
    subView.updateGrid(list)
  }

  private fun pedidoCompraFornecedors(): List<PedidoCompraFornecedor> {
    val filtro = subView.filtro()
    val list = PedidoCompraFornecedor.findAll(filtro).filter {
      (!it.fornecedor.startsWith("ENGECOPI")) && (it.vlPendente > 0.00)
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

  override fun findLine(produto: PedidoCompraProduto): Line? {
    val dataLine = fileText?.listLinesDados() ?: return null
    val refno = produto.refno ?: ""
    val refFab = produto.refFab ?: ""
    val retLine = dataLine.find(refno) ?: dataLine.find(refFab)
    return retLine
  }

  override fun pedidoOK(): Boolean {
    return fileText != null
  }

  override fun isConf(): Boolean {
    return true
  }

  override fun setFileText(fileText: FileText) {
    this.fileText = fileText
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
        document.addSheet(pedidos.flatMap { it.produtos }.sortedWith(compareBy({ it.vendno }, { it.loja }, {
          it.dataPedido
        }, { it.codigo })))
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