package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FiltroFornecedor
import br.com.astrosoft.devolucao.model.beans.FornecedorDesconto
import br.com.astrosoft.framework.viewmodel.ITabView

class TabDescontoViewModel(val viewModel: Devolucao01ViewModel) {
  val subView
    get() = viewModel.view.tabDesconto

  fun updateView() = viewModel.exec {
    val filtro = FiltroFornecedor(query = "", email = false)
    val list = FornecedorDesconto.findAll(filtro)
    subView.updateGrid(list)
  }

  fun updateFiltro() = viewModel.exec {
    val filtro = subView.filtro()
    val list = FornecedorDesconto.findAll(filtro)
    subView.updateGrid(list)
  }
}

interface ITabDesconto : ITabView {
  fun updateGrid(itens: List<FornecedorDesconto>)
  fun itensSelecionados(): List<FornecedorDesconto>
  fun filtro(): FiltroFornecedor
}