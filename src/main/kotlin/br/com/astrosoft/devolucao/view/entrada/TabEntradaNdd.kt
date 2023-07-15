package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabEntradaNddViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabEntradaNddViewModel
import br.com.astrosoft.framework.model.IUser

class TabEntradaNdd(viewModel: TabEntradaNddViewModel) : TabAbstractEntradaNdd<ITabEntradaNddViewModel>(viewModel),
  ITabEntradaNddViewModel {

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaNdd == true
  }

  override val label: String
    get() = "Ndd"
}
