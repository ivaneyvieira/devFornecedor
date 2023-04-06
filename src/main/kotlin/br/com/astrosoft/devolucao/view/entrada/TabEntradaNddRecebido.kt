package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabEntradaNddRecebidoViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabEntradaNddRecebidoViewModel
import br.com.astrosoft.framework.model.IUser

class TabEntradaNddRecebido(viewModel: TabEntradaNddRecebidoViewModel) :
  TabAbstractEntradaNdd<ITabEntradaNddRecebidoViewModel>(viewModel), ITabEntradaNddRecebidoViewModel {
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaNddRecebido == true
  }

  override val label: String
    get() = "Recebido"
}

