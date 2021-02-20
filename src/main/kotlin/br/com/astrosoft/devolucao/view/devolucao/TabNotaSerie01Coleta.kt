package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01Coleta
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01ColetaViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie01Coleta(viewModel: TabNotaSerie01ColetaViewModel): TabDevolucaoAbstract(viewModel),
                                                                      ITabNotaSerie01Coleta {
  override val label: String
    get() = "Notas série 1 Coleta"
  
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.nota01Coleta == true
  }
}