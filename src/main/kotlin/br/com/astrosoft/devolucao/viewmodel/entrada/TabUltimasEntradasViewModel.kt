package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasNdd
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaUltimaNota
import br.com.astrosoft.devolucao.model.reports.RelatorioUltimasNotas
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabUltimasEntradasViewModel(val viewModel: EntradaViewModel) {
  private var filtro: FiltroUltimaNotaEntrada? = null
  val subView
    get() = viewModel.view.tabUltimasEntradasViewModel

  fun openDlgRelatorio() = viewModel.exec {
    subView.openRelatorio()
  }

  fun findNotas(filtro: FiltroUltimaNotaEntrada): List<UltimaNotaEntrada> {
    return UltimaNotaEntrada.findAll(filtro)
  }

  fun imprimeRelatorio(listNotas: List<UltimaNotaEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioUltimasNotas.processaRelatorio(listNotas)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun geraPlanilha(notas: List<UltimaNotaEntrada>): ByteArray {
    val planilha = PlanilhaUltimaNota()
    return planilha.grava(notas)
  }
}

interface ITabUltimasEntradasViewModel : ITabView {
  fun setFIltro(filtro: FiltroUltimaNotaEntrada)
  fun getFiltro(): FiltroUltimaNotaEntrada
  fun openRelatorio()
}