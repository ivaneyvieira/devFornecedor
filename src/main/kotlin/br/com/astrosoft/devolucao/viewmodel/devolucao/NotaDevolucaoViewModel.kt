package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao

class NotaDevolucaoViewModel(val viewModel: DevFornecedorViewModel) {
  private val subView
    get() = viewModel.view.tabNotaDevolucao
  
  fun imprimirNotaDevolucao() {
    TODO("Not yet implemented")
  }
  
  fun updateGridNotaDevolucao() {
    subView.updateGrid(listNotaDevolucao())
  }
  
  private fun listNotaDevolucao(): List<NotaDevolucao> {
    return NotaDevolucao.all()
  }
}

interface INotaDevolucao {
  fun updateGrid(list: List<NotaDevolucao>)
}