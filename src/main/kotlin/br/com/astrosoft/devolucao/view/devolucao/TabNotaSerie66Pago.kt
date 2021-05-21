package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao66View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie66Pago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie66PagoViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie66Pago(viewModel: TabNotaSerie66PagoViewModel) : TabDevolucaoAbstract<IDevolucao66View>(viewModel),
                                                                   ITabNotaSerie66Pago {
  override val label: String
    get() = "Notas Pagas"

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.nota66Pago == true
  }
}