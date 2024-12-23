package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAvariaRecView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAvariaRecTransportadora
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAvariaRecTransportadoraViewModel
import br.com.astrosoft.framework.model.IUser

class TabAvariaRecTransportadora(viewModel: TabAvariaRecTransportadoraViewModel) :
  TabAvariaRecAbstract<IDevolucaoAvariaRecView>(viewModel),
  ITabAvariaRecTransportadora {
  override val label: String
    get() = "Transportadora"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.avariaRecTransportadora == true
  }

  override val situacaoPedido
    get() = listOf(
      NFD_AUTOZ, TRANSPORTADORA, REPOSTO, BAIXA, PAGO, RETORNO,
      PERCA, DESCARTE, ASSISTENCIA_RETORNO, ACERTO
    )
}
