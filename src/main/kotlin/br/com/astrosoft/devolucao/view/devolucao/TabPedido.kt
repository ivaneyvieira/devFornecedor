package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedido(viewModel: TabPedidoViewModel) : TabDevolucaoAbstract<IDevolucao01View>(viewModel), ITabPedido {
  override val label: String
    get() = "Pedido"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedido == true
  }

  override val situacaoPedido
    get() = ESituacaoPedido.values().toList() - ESituacaoPedido.NFD_AUTOZ
}