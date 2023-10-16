package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class EntradaViewModel(view: IEntradaView) : ViewModel<IEntradaView>(view) {

  val tabEntradaNddViewModel = TabEntradaNddViewModel(this)
  val tabEntradaNddReceberViewModel = TabEntradaNddReceberViewModel(this)
  val tabEntradaNddRecebidoViewModel = TabEntradaNddRecebidoViewModel(this)
  val tabTribFiscalViewModel = TabTribFiscalViewModel(this)
  val tabSubstiFcViewModel = TabSubstiFcViewModel(this)
  val tabSpedViewModel = TabSpedViewModel(this)
  val tabSped2ViewModel = TabSped2ViewModel(this)
  val tabSTEstadoViewModel = TabSTEstadoViewModel(this)
  val tabFreteViewModel = TabFreteViewModel(this)
  val tabFretePerViewModel = TabFretePerViewModel(this)
  val tabCteViewModel = TabCteViewModel(this)
  val tabPrecoViewModel = TabPrecoViewModel(this)
  val tabFileNFEViewModel = TabFileNFEViewModel(this)
  val tabRefFiscalViewModel = TabRefFiscalViewModel(this)
  val tabXmlTribViewModel = TabXmlTribViewModel(this)
  val tabTodasEntradasViewModel = TabTodasEntradasViewModel(this)

  override fun listTab() = listOf(
    view.tabEntradaNddViewModel,
    view.tabEntradaNddReceberViewModel,
    view.tabEntradaNddRecebidoViewModel,
    view.tabTribFiscalViewModel,
    view.tabRefFiscalViewModel,
    view.tabSubstiFcViewModel,
    view.tabSpedViewModel,
    view.tabSped2ViewModel,
    view.tabXmlTribViewModel,
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
  val tabSubstiFcViewModel: ITabSubstiFcViewModel
  val tabSpedViewModel: ITabSpedViewModel
  val tabSped2ViewModel: ITabSped2ViewModel
  val tabSTEstadoViewModel: ITabSTEstadoViewModel
  val tabFretePerViewModel: ITabFretePerViewModel
  val tabCteViewModel: ITabCteViewModel
  val tabFreteViewModel: ITabFreteViewModel
  val tabPrecoViewModel: ITabPrecoViewModel
  val tabFileNFEViewModel: ITabFileNFEViewModel
  val tabRefFiscalViewModel: ITabRefFiscalViewModel
  val tabXmlTribViewModel: ITabXmlTribViewModel
  val tabTodasEntradasViewModel: ITabTodasEntradasViewModel
}

