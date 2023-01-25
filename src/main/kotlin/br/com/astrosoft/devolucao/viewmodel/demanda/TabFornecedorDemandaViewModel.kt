package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.framework.viewmodel.ITabView

class TabFornecedorDemandaViewModel(val viewModel: DemandaViewModel) {
  fun updateView() {
    val filtro = subView.filtro()
    val lista = FornecedorProduto.findAll(filtro)
    subView.updateGrid(lista)
  }

  val subView
    get() = viewModel.view.tabFornecedorDemanda
}

interface ITabFornecedorDemanda : ITabView {
  fun updateGrid(itens: List<FornecedorProduto>)
  fun filtro(): String
}

