package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabSapViewModel(val viewModel: Devolucao01ViewModel) {
  val subView
    get() = viewModel.view.tabSap

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

  fun imprimirRelatorio(notas: List<NotaSaida>, labelTitle: String) = viewModel.exec {
    notas.ifEmpty {
      fail("Não nenhuma nota selecionada")
    }
    subView.imprimeRelatorio(notas, labelTitle)
  }

  fun imprimirRelatorioSap(notas: List<NotaDevolucaoSap>, labelTitle: String) = viewModel.exec {
    notas.ifEmpty {
      fail("Não nenhuma nota selecionada")
    }
    subView.imprimeRelatorioSap(notas, labelTitle)
  }
}

interface ITabSap : ITabView {
  fun filtro(): String
  fun updateGrid(itens: List<FornecedorSap>)
  fun imprimeRelatorio(notas: List<NotaSaida>, labelTitle: String)
  fun imprimeRelatorioSap(notas: List<NotaDevolucaoSap>, labelTitle: String)
}