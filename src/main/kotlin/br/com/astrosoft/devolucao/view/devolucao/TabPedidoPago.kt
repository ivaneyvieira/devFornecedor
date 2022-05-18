package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoPago(viewModel: TabPedidoPagoViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoPago {
  override val label: String
    get() = "Pago"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null
  override val situacaoPedido
    get() =  listOf(ESituacaoPedido.PAGO)

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoPago == true
  }
}