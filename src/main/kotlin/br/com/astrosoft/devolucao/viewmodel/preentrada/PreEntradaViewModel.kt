package br.com.astrosoft.devolucao.viewmodel.preentrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class PreEntradaViewModel(view: IPreEntradaView) : ViewModel<IPreEntradaView>(view) {

  val tabPreEntViewModel = TabPreEntViewModel(this)
  val tabPreEntFiscalViewModel = TabPreEntFiscalViewModel(this)

  override fun listTab() = listOf(
    view.tabPreEntViewModel,view.tabPreEntFiscalViewModel,
  )
}

interface IPreEntradaView : IView {
  val tabPreEntViewModel: ITabPreEntViewModel
  val tabPreEntFiscalViewModel: ITabPreEntFiscalViewModel
}

