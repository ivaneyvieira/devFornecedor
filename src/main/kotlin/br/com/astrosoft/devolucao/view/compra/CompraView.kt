package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.compra.CompraViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.ICompraView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Compra")
@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class CompraView : ViewLayout<CompraViewModel>(), ICompraView {
  override val viewModel: CompraViewModel = CompraViewModel(this)
  override val tabPedidosViewModel = TabPedidos(viewModel.tabPedidosViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuCompra
  }

  init {
    addTabSheat(viewModel)
  }
}