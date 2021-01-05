package br.com.astrosoft.devolucao.viewmodel.recebimento

import br.com.astrosoft.devolucao.model.beans.FornecedorEntrada
import br.com.astrosoft.framework.viewmodel.ITabView

class TabNotaPendenteViewModel(val viewModel: RecebimentoViewModel) {
  private val subView
    get() = viewModel.view.tabNotaPendente
  
  fun updateGridNota() = viewModel.exec {
    subView.updateGrid(listFornecedores())
  }
  
  fun listFornecedores() : List<FornecedorEntrada>{
    return FornecedorEntrada.listFornecedores()
  }
  
}

interface ITabNotaPendente: ITabView {
  fun updateGrid(listFornecedores: List<FornecedorEntrada>)
}