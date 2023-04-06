package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01ViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie01(viewModel: TabNotaSerie01ViewModel) : TabDevolucaoAbstract<IDevolucao01View>(viewModel),
  ITabNotaSerie01 {
  override val label: String
    get() = "Abertas"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.nota01 == true
  }
}