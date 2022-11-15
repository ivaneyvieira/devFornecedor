package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.FiltroPedidoCompra
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.framework.viewmodel.ITabView

class TabPedidosViewModel(val viewModel: CompraViewModel) {
  val subView = viewModel.view.tabPedidosViewModel

  fun updateComponent() = viewModel.exec {
    val filtro = subView.filtro()
    val list = PedidoCompraFornecedor.findAll(filtro)
    subView.updateGrid(list)
  }

  fun listLojas(): List<Loja> {
    val list = Loja.allLojas() + Loja.lojaZero
    return list.sortedBy { it.no }
  }
}

interface ITabPedidosViewModel : ITabView {
  fun filtro(): FiltroPedidoCompra
  fun updateGrid(itens: List<PedidoCompraFornecedor>)
}