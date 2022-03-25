package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoAjuste(viewModel: TabPedidoAjusteViewModel) : TabDevolucaoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoAjuste {
  override val label: String
    get() = "Ajuste"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null
  override val situacaoPedido
    get() = listOf(ESituacaoPedido.AJUSTE_GARANTIA)

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoAjuste == true
  }
}