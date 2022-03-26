package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoInternaView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAjusteGarantia
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAjusteGarantiaViewModel
import br.com.astrosoft.framework.model.IUser

class TabAjusteGarantia(viewModel: TabAjusteGarantiaViewModel) : TabDevolucaoAbstract<IDevolucaoInternaView>(viewModel),
        ITabAjusteGarantia {
  override val label: String
    get() = "Base"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.ajusteGarantia == true
  }
}