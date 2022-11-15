package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class CompraViewModel(view: ICompraView) : ViewModel<ICompraView>(view)  {
  val tabPedidosViewModel = TabPedidosViewModel(this)


  override fun listTab() = listOf(
    view.tabPedidosViewModel,
                                 )
}

interface ICompraView : IView {
  val tabPedidosViewModel: ITabPedidosViewModel
}
