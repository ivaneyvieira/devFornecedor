package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie66
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie66ViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie66(viewModel: TabNotaSerie66ViewModel): TabDevolucaoAbstract(viewModel), ITabNotaSerie66 {
  override val label: String
    get() = "Notas série 66"
  
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.nota66 == true
  }
}