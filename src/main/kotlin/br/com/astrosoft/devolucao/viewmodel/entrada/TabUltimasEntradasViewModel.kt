package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.framework.viewmodel.ITabView

class TabUltimasEntradasViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabUltimasEntradasViewModel

  fun openDlgRelatorio() = viewModel.exec {
    subView.openRelatorio()
  }

  fun findNotas(filtro : FiltroUltimaNotaEntrada) : List<UltimaNotaEntrada>{
    return UltimaNotaEntrada.findAll(filtro)
  }
}

interface ITabUltimasEntradasViewModel : ITabView {
  fun setFIltro(filtro: FiltroUltimaNotaEntrada)
  fun getFiltro(): FiltroUltimaNotaEntrada
  fun openRelatorio()
}