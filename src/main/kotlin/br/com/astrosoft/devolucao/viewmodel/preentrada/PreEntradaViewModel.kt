package br.com.astrosoft.devolucao.viewmodel.preentrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class PreEntradaViewModel(view: IPreEntradaView) : ViewModel<IPreEntradaView>(view) {

  val tabTribFiscalPreViewModel = TabTribFiscalPreViewModel(this)

  override fun listTab() = listOf(
    view.tabTribFiscalPreViewModel,
  )
}

interface IPreEntradaView : IView {
  val tabTribFiscalPreViewModel: ITabTribFiscalPreViewModel
}

