package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido.*
import br.com.astrosoft.framework.model.IUser

class TabAvariaRecFinalizado(viewModel: TabAvariaRecFinalizadoViewModel) : TabAvariaRecAbstract<IDevolucaoAvariaRecView>(viewModel),
  ITabAvariaRecFinalizado {
  override val label: String
    get() = "Finalizado"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.avariaRecFinalizado == true
  }

  override val situacaoPedido
    get() = listOf(
      NFD_AUTOZ, BAIXA, PAGO, RETORNO,
      PERCA, DESCARTE, ASSISTENCIA_RETORNO
    )
}
