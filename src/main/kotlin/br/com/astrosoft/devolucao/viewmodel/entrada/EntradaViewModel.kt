package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class EntradaViewModel(view: IEntradaView) : ViewModel<IEntradaView>(view) {

  val tabEntradaNddViewModel = TabEntradaNddViewModel(this)
  val tabEntradaNddReceberViewModel = TabEntradaNddReceberViewModel(this)
  val tabEntradaNddRecebidoViewModel = TabEntradaNddRecebidoViewModel(this)
  val tabNfPrecViewModel = TabNfPrecViewModel(this)
  val tabTodasEntradasViewModel = TabTodasEntradasViewModel(this)

  override fun listTab() = listOf(
    view.tabEntradaNddViewModel,
    view.tabEntradaNddReceberViewModel,
    view.tabEntradaNddRecebidoViewModel,
    view.tabNfPrecViewModel,
    view.tabTodasEntradasViewModel,
                                 )
}

interface IEntradaView : IView {
  val tabEntradaNddViewModel: ITabEntradaNddViewModel
  val tabEntradaNddRecebidoViewModel: ITabEntradaNddRecebidoViewModel
  val tabEntradaNddReceberViewModel: ITabEntradaNddReceberViewModel
  val tabNfPrecViewModel: ITabNfPrecViewModel
  val tabTodasEntradasViewModel: ITabTodasEntradasViewModel
}

