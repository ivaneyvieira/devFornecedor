package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorCompra
import br.com.astrosoft.devolucao.model.reports.RelatorioPedidoCompra
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabConferirViewModel(val viewModel: CompraViewModel) : ITabCompraViewModel {
  val subView
    get() = viewModel.view.tabConferirViewModel

  fun updateComponent() = viewModel.exec {
    val filtro = subView.filtro()
    val list = PedidoCompraFornecedor.findAll(filtro)
    subView.updateGrid(list)
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
    else RelatorioPedidoCompra.processaExcel(pedidos)
  }

  fun excelRelatorioFornecedor(pedidos: List<PedidoCompra>): ByteArray {
    return if (pedidos.isEmpty()) {
      viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    }
    else RelatorioFornecedorCompra.processaExcel(pedidos)
  }
}

interface ITabConferirViewModel : ITabView {
  fun filtro(): FiltroPedidoCompra
  fun updateGrid(itens: List<PedidoCompraFornecedor>)
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
  fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>)
  fun imprimeSelecionados(pedidos: List<PedidoCompra>)
}