package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.framework.viewmodel.ITabView

class TabPedidoFornecedorViewModel(val viewModel: DevolucaoPedidoViewModel) {
  fun updateView() {
    val filtro = subView.filtro()
    val lista = FornecedorProduto.findAll(filtro)
    subView.updateGrid(lista)
  }

  val subView: ITabPedidoFornecedor
    get() = TODO() //viewModel.view.tabPedidoFornecedor
}

interface ITabPedidoFornecedor : ITabView {
  fun updateGrid(itens: List<FornecedorProduto>)
  fun filtro(): String
}

