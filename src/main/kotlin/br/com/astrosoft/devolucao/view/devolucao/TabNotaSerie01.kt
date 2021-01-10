package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01ViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie01(viewModel: TabNotaSerie01ViewModel): TabDevolucaoAbstract(viewModel), ITabNotaSerie01 {
  override val label: String
    get() = "Notas série 1"
  
  override fun isAuthorized(user : IUser): Boolean {
    val username =user as? UserSaci
    return username?.nota01 == true
  }
}