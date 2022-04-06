package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPedidoView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedidoPago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoPagoViewModel
import br.com.astrosoft.framework.model.IUser

class TabPedidoPago(viewModel: TabPedidoPagoViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoPago {
  override val label: String
    get() = "Pedido Pago"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoPago == true
  }
}