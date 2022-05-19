package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoBaixa(viewModel: TabPedidoBaixaViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoBaixa {
  override val label: String
    get() = "Baixa"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null
  override val situacaoPedido
    get() = listOf(ESituacaoPedido.BAIXA)

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return false //username?.pedidoBaixa == true
  }
}