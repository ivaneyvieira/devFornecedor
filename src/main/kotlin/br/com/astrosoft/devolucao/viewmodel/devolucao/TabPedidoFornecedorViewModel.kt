package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.framework.viewmodel.ITabView

class TabPedidoFornecedorViewModel(val viewModel: DevolucaoPedidoViewModel) {
  fun updateView() {
    val lista = FornecedorProduto.findAll()
    subView.updateGrid(lista)
  }

  val subView
    get() = viewModel.view.tabPedidoFornecedor
}

interface ITabPedidoFornecedor : ITabView {
  fun updateGrid(itens: List<FornecedorProduto>)
}

