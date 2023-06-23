package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.FiltroPedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabConferirViewModel(viewModel: CompraViewModel) : TabAbstractConfViewModel(viewModel) {
  private val listPedidosFornecedor = mutableListOf<PedidoCompraFornecedor>()

  val subView
    get() = viewModel.view.tabConferirViewModel

  override fun imprimirPedidoCompra(pedidos: List<PedidoCompra>) = viewModel.exec {
    pedidos.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimeSelecionados(pedidos)
  }

  override fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>) = viewModel.exec {
    pedido.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimirRelatorioFornecedor(pedido)
  }

  override fun updateComponent() = viewModel.exec {
    val list = pedidoCompraFornecedors()
    listPedidosFornecedor.clear()
    listPedidosFornecedor.addAll(list)
    subView.updateGrid(list)
  }

  override fun listPedidosFornecedor(): List<PedidoCompraFornecedor> {
    return listPedidosFornecedor
  }

  override fun tipoPainel(): ETipoPainel {
    return ETipoPainel.Conferir
  }

  private fun pedidoCompraFornecedors(): List<PedidoCompraFornecedor> {
    val filtro = subView.filtro()
    val list = PedidoCompraFornecedor.findAll(filtro).filter {
      (!it.fornecedor.startsWith("ENGECOPI"))
    }
    return list
  }

  fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Nenhuma fornecedor foi selecionada")
    }
    subView.imprimirRelatorioResumido(fornecedores)
  }
}

interface ITabConferirViewModel : ITabView {
  fun filtro(): FiltroPedidoCompra
  fun updateGrid(itens: List<PedidoCompraFornecedor>)
  fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>)
  fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>)
  fun imprimeSelecionados(pedidos: List<PedidoCompra>)
}