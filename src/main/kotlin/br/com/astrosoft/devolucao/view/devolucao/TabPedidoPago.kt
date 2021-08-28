package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedidoPago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoPagoViewModel
import br.com.astrosoft.framework.model.IUser

class TabPedidoPago(viewModel: TabPedidoPagoViewModel) : TabDevolucaoAbstract<IDevolucao01View>(viewModel),
        ITabPedidoPago {
  override val label: String
    get() = "Pedido Pago"

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedido == true
  }
}