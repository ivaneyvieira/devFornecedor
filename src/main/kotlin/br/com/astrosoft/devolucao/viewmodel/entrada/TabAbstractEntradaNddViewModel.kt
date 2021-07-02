package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaFornecedorNdd
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasNdd
import br.com.astrosoft.devolucao.model.reports.DanfeReport
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail
import java.time.LocalDate

abstract class TabAbstractEntradaNddViewModel<T : ITabAbstractEntradaNddViewModel>(val viewModel: EntradaViewModel) {
  abstract val subView: T
  abstract val tipoTab: ETipoNota

  fun updateView() {
    val query: String = subView.query()
    val dataInicial = subView.dataInicial() ?: LocalDate.of(2006, 1, 1)
    val dataFinal = subView.dataFinal() ?: LocalDate.now()
    val filtro = FiltroEntradaNdd(query, tipoTab, dataInicial, dataFinal)
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

  fun salvaNotaEntrada(bean: NotaEntradaNdd?) = viewModel.exec {
    bean?.save()
  }

  fun createDanfe(nota: NotaEntradaNdd) {
    val itensNotaReport = nota.itensNotaReport()
    val report = DanfeReport.create(itensNotaReport)
    viewModel.view.showReport("Danfee", report)
  }
}

interface ITabAbstractEntradaNddViewModel : ITabView {
  fun query(): String
  fun dataInicial(): LocalDate?
  fun dataFinal(): LocalDate?
  fun updateGrid(itens: List<FornecedorNdd>)
  fun imprimeRelatorio(fornecedores: List<FornecedorNdd>)
  fun imprimeRelatorioResumido(fornecedores: List<FornecedorNdd>)
}