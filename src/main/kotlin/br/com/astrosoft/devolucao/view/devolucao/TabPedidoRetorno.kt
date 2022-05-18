package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoRetorno(viewModel: TabPedidoRetornoViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoRetorno {
  override val label: String
    get() = "Retorno"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null
  override val situacaoPedido
    get() =  listOf(ESituacaoPedido.RETORNO)

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoRetorno == true
  }
}