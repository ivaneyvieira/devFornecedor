package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.compra.CompraViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.ICompraView
import br.com.astrosoft.devolucao.viewmodel.compra.ITabPedidosViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout

class CompraView: ViewLayout<CompraViewModel>(), ICompraView {
  override val viewModel= CompraViewModel(this)

  override val tabPedidosViewModel = TabPedidos(viewModel.tabPedidosViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuCompra
  }

  init {
    addTabSheat(viewModel)
  }
}