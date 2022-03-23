package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabAjusteGarantiaPerca(viewModel: TabAjusteGarantiaPercaViewModel) :
        TabDevolucaoAbstract<IDevolucaoInternaView>(viewModel), ITabAjusteGarantiaPerca {
  override val label: String
    get() = "Ajuste Gar Perca"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.ajusteGarantiaPerca == true
  }
}