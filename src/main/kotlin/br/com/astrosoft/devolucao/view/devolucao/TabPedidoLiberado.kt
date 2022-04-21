package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoLiberado(viewModel: TabPedidoLiberadoViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoLiberado {
  override val label: String
    get() = "Liberado"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null
  override val situacaoPedido
    get() = listOf(ESituacaoPedido.LIBERADO)

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoLiberado == true
  }
}