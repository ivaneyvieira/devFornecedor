package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasSap
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

  fun imprimirRelatorio(fornecedores: List<FornecedorSap>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Não nenhum fornecedor selecionado")
    }
    subView.imprimeRelatorio(fornecedores)
  }

  fun geraPlanilha(fornecedores: List<FornecedorSap>): ByteArray? {
    val planilha = PlanilhaNotasSap()
    val notas = fornecedores.flatMap { it.notas }
    return planilha.grava(notas)
  }
}

interface ITabSap : ITabView {
  fun filtro(): String
  fun updateGrid(itens: List<FornecedorSap>)
  fun imprimeRelatorio(fornecedores: List<FornecedorSap>)
}