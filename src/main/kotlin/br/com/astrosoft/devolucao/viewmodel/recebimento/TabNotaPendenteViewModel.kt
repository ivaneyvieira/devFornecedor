package br.com.astrosoft.devolucao.viewmodel.recebimento

import br.com.astrosoft.devolucao.model.beans.FornecedorEntrada
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate

class TabNotaPendenteViewModel(val viewModel: RecebimentoViewModel) : IViewModelUpdate {
  private val subView
    get() = viewModel.view.tabNotaPendente

  override fun updateView() = viewModel.exec {
    subView.updateGrid(listFornecedores())
  }

  private fun listFornecedores(): List<FornecedorEntrada> {
    return FornecedorEntrada.listFornecedores()
  }
}

interface ITabNotaPendente : ITabView {
  fun updateGrid(itens: List<FornecedorEntrada>)
}
