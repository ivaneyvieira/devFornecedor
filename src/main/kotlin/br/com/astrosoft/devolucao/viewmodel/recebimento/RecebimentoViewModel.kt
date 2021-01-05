package br.com.astrosoft.devolucao.viewmodel.recebimento

import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class RecebimentoViewModel(view: IRecebimentoView): ViewModel<IRecebimentoView>(view) {
  val tabNotaPendenteViewModel = TabNotaPendenteViewModel(this)
  
  private val listTab: List<ITabView>
    get() = listOf(view.tabNotaPendente)
  
  fun tabsAuthorized() = listTab.filter {
    it.isAuthorized()
  }
}

interface IRecebimentoView: IView {
  val tabNotaPendente: ITabNotaPendente
}