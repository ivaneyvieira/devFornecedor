package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoViewModel
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
}