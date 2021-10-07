package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPendenteView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaPendenteSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaPendenteSerie01ViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaPendenteSerie01(viewModel: TabNotaPendenteSerie01ViewModel) :
        TabDevolucaoAbstract<IDevolucaoPendenteView>(viewModel), ITabNotaPendenteSerie01 {
  override val label: String
    get() = "Notas"

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.admin == true
  }
}