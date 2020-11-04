package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.framework.viewmodel.fail

class NotaDevolucaoViewModel(val viewModel: DevFornecedorViewModel) {
  private val subView
    get() = viewModel.view.tabNotaDevolucao
  
  fun imprimirNotaDevolucao(nota : NotaDevolucao) = viewModel.exec{
    subView.imprimeSelecionados(listOf(nota))
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
  fun itensSelecionados(): List<NotaDevolucao>
  fun imprimeSelecionados(itens : List<NotaDevolucao>)
}