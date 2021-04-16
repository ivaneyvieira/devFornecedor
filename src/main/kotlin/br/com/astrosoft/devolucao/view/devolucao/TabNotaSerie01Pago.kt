package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01Pago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01PagoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01ViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie01Pago(viewModel: TabNotaSerie01PagoViewModel) : TabDevolucaoAbstract(viewModel),
                                                                   ITabNotaSerie01Pago {
  override val label: String
    get() = "Notas Série 1 Pago"

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.nota01 == true
  }
}