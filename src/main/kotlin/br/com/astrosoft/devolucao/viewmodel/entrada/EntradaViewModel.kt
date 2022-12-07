package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class EntradaViewModel(view: IEntradaView) : ViewModel<IEntradaView>(view) {

  val tabEntradaNddViewModel = TabEntradaNddViewModel(this)
  val tabEntradaNddReceberViewModel = TabEntradaNddReceberViewModel(this)
  val tabEntradaNddRecebidoViewModel = TabEntradaNddRecebidoViewModel(this)
  val tabNfPrecFiscalViewModel = TabNfPrecFiscalViewModel(this)
  val tabFreteViewModel = TabFreteViewModel(this)
  val tabFretePerViewModel = TabFretePerViewModel(this)
  val tabPrecoViewModel = TabPrecoViewModel(this)
  val tabPrecoPreRecViewModel = TabPrecoPreRecViewModel(this)
  val tabNfPrecInfoViewModel = TabNfPrecInfoViewModel(this)
  val tabTodasEntradasViewModel = TabTodasEntradasViewModel(this)

  override fun listTab() = listOf(
    view.tabEntradaNddViewModel,
    view.tabEntradaNddReceberViewModel,
    view.tabEntradaNddRecebidoViewModel,
    view.tabNfPrecFiscalViewModel,
    view.tabNfPrecInfoViewModel,
    view.tabTodasEntradasViewModel,
    view.tabFreteViewModel,
    view.tabPrecoViewModel,
    view.tabPrecoPreRecViewModel,
    view.tabFretePerViewModel,
                                 )
}

interface IEntradaView : IView {
  val tabEntradaNddViewModel: ITabEntradaNddViewModel
  val tabEntradaNddRecebidoViewModel: ITabEntradaNddRecebidoViewModel
  val tabEntradaNddReceberViewModel: ITabEntradaNddReceberViewModel
  val tabNfPrecFiscalViewModel: ITabNfPrecFiscalViewModel
  val tabFretePerViewModel: ITabFretePerViewModel
  val tabFreteViewModel: ITabFreteViewModel
  val tabPrecoViewModel: ITabPrecoViewModel
  val tabPrecoPreRecViewModel: ITabPrecoPreRecViewModel
  val tabNfPrecInfoViewModel: ITabNfPrecInfoViewModel
  val tabTodasEntradasViewModel: ITabTodasEntradasViewModel
}

