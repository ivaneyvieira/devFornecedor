package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabEntradaNddReceberViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabEntradaNddReceberViewModel
import br.com.astrosoft.framework.model.IUser

class TabEntradaNddReceber(viewModel: TabEntradaNddReceberViewModel) :
  TabAbstractEntradaNdd<ITabEntradaNddReceberViewModel>(viewModel), ITabEntradaNddReceberViewModel {
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaNddReceber == true
  }

  override val label: String
    get() = "Receber"
}
