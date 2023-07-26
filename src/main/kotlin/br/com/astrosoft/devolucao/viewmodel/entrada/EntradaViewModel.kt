package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class EntradaViewModel(view: IEntradaView) : ViewModel<IEntradaView>(view) {

  val tabEntradaNddViewModel = TabEntradaNddViewModel(this)
  val tabEntradaNddReceberViewModel = TabEntradaNddReceberViewModel(this)
  val tabEntradaNddRecebidoViewModel = TabEntradaNddRecebidoViewModel(this)
  val tabTribFiscalViewModel = TabTribFiscalViewModel(this)
  val tabSpedViewModel = TabSpedViewModel(this)
  val tabSped2ViewModel = TabSped2ViewModel(this)
  val tabSTEstadoViewModel = TabSTEstadoViewModel(this)
  val tabFreteViewModel = TabFreteViewModel(this)
  val tabFretePerViewModel = TabFretePerViewModel(this)
  val tabCteViewModel = TabCteViewModel(this)
  val tabPrecoViewModel = TabPrecoViewModel(this)
  val tabFileNFEViewModel = TabFileNFEViewModel(this)
  val tabRefFiscalViewModel = TabRefFiscalViewModel(this)
  val tabTodasEntradasViewModel = TabTodasEntradasViewModel(this)

  override fun listTab() = listOf(
    view.tabEntradaNddViewModel,
    view.tabEntradaNddReceberViewModel,
    view.tabEntradaNddRecebidoViewModel,
    view.tabTribFiscalViewModel,
    view.tabRefFiscalViewModel,
    view.tabSpedViewModel,
    view.tabSped2ViewModel,
    view.tabSTEstadoViewModel,
    view.tabTodasEntradasViewModel,
    view.tabFreteViewModel,
    view.tabPrecoViewModel,
    view.tabFretePerViewModel,
    view.tabCteViewModel,
    view.tabFileNFEViewModel,
  )
}

interface IEntradaView : IView {
  val tabEntradaNddViewModel: ITabEntradaNddViewModel
  val tabEntradaNddRecebidoViewModel: ITabEntradaNddRecebidoViewModel
  val tabEntradaNddReceberViewModel: ITabEntradaNddReceberViewModel
  val tabTribFiscalViewModel: ITabTribFiscalViewModel
  val tabSpedViewModel: ITabSpedViewModel
  val tabSped2ViewModel: ITabSped2ViewModel
  val tabSTEstadoViewModel: ITabSTEstadoViewModel
  val tabFretePerViewModel: ITabFretePerViewModel
  val tabCteViewModel: ITabCteViewModel
  val tabFreteViewModel: ITabFreteViewModel
  val tabPrecoViewModel: ITabPrecoViewModel
  val tabFileNFEViewModel: ITabFileNFEViewModel
  val tabRefFiscalViewModel: ITabRefFiscalViewModel
  val tabTodasEntradasViewModel: ITabTodasEntradasViewModel
}

