package br.com.astrosoft.devolucao.viewmodel.saida

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class SaidaViewModel(view: ISaidaView) : ViewModel<ISaidaView>(view) {

  val tabSaidaNddViewModel = TabSaidaNddViewModel(this)

  override fun listTab() = listOf(view.tabSaidaNddViewModel)
}

interface ISaidaView : IView {
  val tabSaidaNddViewModel: ITabSaidaNddViewModel
}

