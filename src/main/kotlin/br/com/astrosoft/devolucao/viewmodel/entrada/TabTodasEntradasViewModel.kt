package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaQuery
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioTodasEntradas
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabTodasEntradasViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabTodasEntradasViewModel

  fun openDlgRelatorio() = viewModel.exec {
    val list = findNotas(subView.getFiltro())
    subView.openRelatorio(list)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroNotaEntradaQuery): List<NotaEntradaQuery> {
    return NotaEntradaQuery.findNotas(filtro)
  }

  fun imprimeRelatorio(listNotas: List<NotaEntradaQuery>) {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioTodasEntradas.processaRelatorio(listNotas)
    viewModel.showReport("nfPrecificacao", relatorio)
  }
}

interface ITabTodasEntradasViewModel : ITabView {
  fun setFiltro(filtro: FiltroNotaEntradaQuery)
  fun getFiltro(): FiltroNotaEntradaQuery
  fun openRelatorio(list: List<NotaEntradaQuery>)
}