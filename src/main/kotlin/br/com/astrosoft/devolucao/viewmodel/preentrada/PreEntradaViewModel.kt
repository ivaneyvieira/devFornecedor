package br.com.astrosoft.devolucao.viewmodel.preentrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class PreEntradaViewModel(view: IPreEntradaView) : ViewModel<IPreEntradaView>(view) {

  val tabTribFiscalPreViewModel = TabTribFiscalPreViewModel(this)
  val tabPreRefFiscalViewModel = TabPreRefFiscalViewModel(this)
  val tabPrePrecoViewModel = TabPrePrecoViewModel(this)

  override fun listTab() = listOf(
    view.tabTribFiscalPreViewModel,
    view.tabPreRefFiscalViewModel,
    view.tabPrePrecoViewModel,
  )
}

interface IPreEntradaView : IView {
  val tabTribFiscalPreViewModel: ITabTribFiscalPreViewModel
  val tabPreRefFiscalViewModel: ITabPreRefFiscalViewModel
  val tabPrePrecoViewModel: ITabPrePrecoViewModel
}

