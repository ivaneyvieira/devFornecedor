package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoPendente(viewModel: TabPedidoPendenteViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoPendente {
  override val label: String
    get() = "Pendente"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoPendente == true
  }

  override val situacaoPedido
    get() = values().toList() - NFD_AUTOZ - EMAIL_ENVIADO
}