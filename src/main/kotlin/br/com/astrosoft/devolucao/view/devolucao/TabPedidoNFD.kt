package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabPedidoNFD(viewModel: TabPedidoNFDViewModel) : TabDevolucaoAbstract<IDevolucaoPedidoView>(viewModel),
        ITabPedidoNFD {
  override val label: String
    get() = "Ped NFD"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null
  override val situacaoPedido
    get() = listOf(ESituacaoPedido.NFD_AUTOZ)

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoNFD == true
  }
}