package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class CompraViewModel(view: ICompraView) : ViewModel<ICompraView>(view) {
  val tabPedidosViewModel = TabPedidosViewModel(this)
  val tabConferirViewModel = TabConferirViewModel(this)
  val tabConferidoViewModel = TabConferidoViewModel(this)

  override fun listTab() = listOf(
    view.tabPedidosViewModel, view.tabConferirViewModel, view.tabConferidoViewModel,
                                 )
}

interface ICompraView : IView {
  val tabPedidosViewModel: ITabPedidosViewModel
  val tabConferirViewModel: ITabConferirViewModel
  val tabConferidoViewModel: ITabConferidoViewModel
}
