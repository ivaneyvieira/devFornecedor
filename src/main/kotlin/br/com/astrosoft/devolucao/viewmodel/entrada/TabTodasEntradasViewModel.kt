package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaQuery
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.framework.viewmodel.ITabView

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
}

interface ITabTodasEntradasViewModel : ITabView {
  fun setFiltro(filtro: FiltroNotaEntradaQuery)
  fun getFiltro(): FiltroNotaEntradaQuery
  fun openRelatorio(list: List<NotaEntradaQuery>)
}