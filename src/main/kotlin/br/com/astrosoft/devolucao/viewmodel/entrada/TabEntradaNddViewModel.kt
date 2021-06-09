package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaFornecedorNdd
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaFornecedorSap
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasNdd
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasSap
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabEntradaNddViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabEntradaNddViewModel

  fun updateView() {
    val filtro: String = subView.filtro()
    val resultList = FornecedorNdd.listFornecedores(filtro)

    subView.updateGrid(resultList)
  }

  fun readExcel(fileName: String?) = viewModel.exec {
    fileName ?: fail("Arquivo não encontrado")
    FornecedorSap.loadSheet(fileName)
    updateView()
  }

  fun imprimirRelatorio(fornecedores: List<FornecedorNdd>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Não nenhum fornecedor selecionado")
    }
    subView.imprimeRelatorio(fornecedores)
  }

  fun imprimirRelatorioResumido(fornecedores: List<FornecedorNdd>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Não nenhum fornecedor selecionado")
    }
    subView.imprimeRelatorioResumido(fornecedores)
  }

  fun geraPlanilha(fornecedores: List<FornecedorNdd>): ByteArray {
    val planilha = PlanilhaNotasNdd()
    val notas = fornecedores.flatMap { it.notas }
    return planilha.grava(notas)
  }

  fun geraPlanilhaResumo(fornecedores: List<FornecedorNdd>): ByteArray {
    val planilha = PlanilhaFornecedorNdd()
    return planilha.grava(fornecedores)
  }
}

interface ITabEntradaNddViewModel : ITabView {
  fun filtro(): String
  fun updateGrid(itens: List<FornecedorNdd>)
  fun imprimeRelatorio(fornecedores: List<FornecedorNdd>)
  fun imprimeRelatorioResumido(fornecedores: List<FornecedorNdd>)
}