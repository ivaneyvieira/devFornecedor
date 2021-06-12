package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.ETipoNota
import br.com.astrosoft.devolucao.model.beans.FiltroEntradaNdd
import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaFornecedorNdd
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasNdd
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

abstract class TabAbstractEntradaNddViewModel<T : ITabAbstractEntradaNddViewModel>(val viewModel: EntradaViewModel) {
  abstract val subView: T
  abstract val tipoTab: ETipoNota

  fun updateView() {
    val query: String = subView.query()
    val filtro = FiltroEntradaNdd(query, tipoTab)
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

interface ITabAbstractEntradaNddViewModel : ITabView {
  fun query(): String
  fun updateGrid(itens: List<FornecedorNdd>)
  fun imprimeRelatorio(fornecedores: List<FornecedorNdd>)
  fun imprimeRelatorioResumido(fornecedores: List<FornecedorNdd>)
}