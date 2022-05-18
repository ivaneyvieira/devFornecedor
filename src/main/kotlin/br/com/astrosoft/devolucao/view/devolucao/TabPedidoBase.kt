package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido.values
import br.com.astrosoft.framework.model.IUser

class TabPedidoBase(viewModel: TabPedidoBaseViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoBase {
  override val label: String
    get() = "Base"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoBase == true
  }

  override val situacaoPedido
    get() = values().toList()
}