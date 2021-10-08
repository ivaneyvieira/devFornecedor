package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao66View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabEntrada
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabEntradaViewModel
import br.com.astrosoft.framework.model.IUser

class TabEntrada(viewModel: TabEntradaViewModel) : TabDevolucaoAbstract<IDevolucao66View>(viewModel), ITabEntrada {
  override val label: String
    get() = "Retorno"
  override val situacaoPendencia: String?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entrada == true
  }
}