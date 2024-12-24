package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAvariaRecView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAvariaRecEmail
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAvariaRecEmailViewModel
import br.com.astrosoft.framework.model.IUser

class TabAvariaRecEmail(viewModel: TabAvariaRecEmailViewModel) :
  TabAvariaRecAbstract<IDevolucaoAvariaRecView>(viewModel), ITabAvariaRecEmail {
  override val label: String
    get() = "Email"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun filtroEmail(): Boolean = true

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.avariaRecEmail == true
  }

  override val situacaoPedido
    get() = listOf(
      NFD_AUTOZ, TRANSPORTADORA, REPOSTO, BAIXA, PAGO, RETORNO,
      PERCA, DESCARTE, ASSISTENCIA_RETORNO, ACERTO
    )
}
