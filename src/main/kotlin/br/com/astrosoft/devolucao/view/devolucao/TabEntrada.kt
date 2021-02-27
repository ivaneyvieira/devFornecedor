package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabEntrada
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabEntradaViewModel
import br.com.astrosoft.framework.model.IUser

class TabEntrada(viewModel: TabEntradaViewModel) : TabDevolucaoAbstract(viewModel), ITabEntrada {
  override val label: String
    get() = "Retorno 66"

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entrada == true
  }
}