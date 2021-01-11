package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie66Pago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie66PagoViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie66Pago(viewModel: TabNotaSerie66PagoViewModel): TabDevolucaoAbstract(viewModel),
                                                                  ITabNotaSerie66Pago {
  override val label: String
    get() = "Notas série 66 Pago"
  
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.nota66Pago == true
  }
}