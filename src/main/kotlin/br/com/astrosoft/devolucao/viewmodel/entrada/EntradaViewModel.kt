package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class EntradaViewModel(view: IEntradaView) : ViewModel<IEntradaView>(view) {

  val tabEntradaNddViewModel = TabEntradaNddViewModel(this)
  val tabEntradaNddReceberViewModel = TabEntradaNddReceberViewModel(this)
  val tabEntradaNddRecebidoViewModel = TabEntradaNddRecebidoViewModel(this)

  override fun listTab() =
          listOf(view.tabEntradaNddViewModel, view.tabEntradaNddReceberViewModel, view.tabEntradaNddRecebidoViewModel)
}

interface IEntradaView : IView {
  val tabEntradaNddViewModel: ITabEntradaNddViewModel
  val tabEntradaNddRecebidoViewModel: ITabEntradaNddRecebidoViewModel
  val tabEntradaNddReceberViewModel: ITabEntradaNddReceberViewModel
}

