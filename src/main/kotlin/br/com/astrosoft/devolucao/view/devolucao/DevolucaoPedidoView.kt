package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoPedidoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPedidoView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Pedido")
@CssImport("./styles/gridTotal.css")
class DevolucaoPedidoView : ViewLayout<DevolucaoPedidoViewModel>(), IDevolucaoPedidoView {
  override val viewModel: DevolucaoPedidoViewModel = DevolucaoPedidoViewModel(this)
  override val tabPedido = TabPedido(viewModel.tabPedidoViewModel)
  override val tabPedidoNFD = TabPedidoNFD(viewModel.tabPedidoNFDViewModel)
  override val tabPedidoPago = TabPedidoPago(viewModel.tabPedidoPagoViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucaoPedido
  }

  init {
    addTabSheat(viewModel)
  }
}

