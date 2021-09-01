package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabDesconto(viewModel: TabDescontoViewModel) : TabDevolucaoAbstract<IDevolucao01View>(viewModel),
        ITabDesconto {
  override val label: String
    get() = "Desconto"

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.desconto == true
  }
}