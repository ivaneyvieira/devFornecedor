package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAjusteGarantia
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAjusteGarantiaViewModel
import br.com.astrosoft.framework.model.IUser

class TabAjusteGarantia(viewModel: TabAjusteGarantiaViewModel) : TabDevolucaoAbstract<IDevolucao01View>(viewModel),
        ITabAjusteGarantia {
  override val label: String
    get() = "Ajuste Garantia"
  override val situacaoPendencia: String?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.ajusteGarantia == true
  }
}