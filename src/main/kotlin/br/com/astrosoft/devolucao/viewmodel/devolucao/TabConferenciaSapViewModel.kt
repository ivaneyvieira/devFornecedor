package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabConferenciaSapViewModel(val viewModel: Devolucao01ViewModel) {
  val subView
    get() = viewModel.view.tabConferenciaSap

  fun updateView() {
    val filtro: String = subView.filtro()
    val resultList = FornecedorSap.findFornecedores(filtro)

    subView.updateGrid(resultList)
  }

  fun readExcel(fileName: String?) = viewModel.exec {
    fileName ?: fail("Arquivo não encontrado")
    FornecedorSap.loadSheet(fileName)
    updateView()
  }
}

interface ITabConferenciaSap : ITabView {
  fun filtro(): String
  fun updateGrid(itens: List<FornecedorSap>)
}