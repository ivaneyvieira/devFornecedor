package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPendenteView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaPendente
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaPendenteViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaPendente(viewModel: TabNotaPendenteViewModel, private val situacao: () -> ESituacaoPendencia?) :
        TabDevolucaoAbstract<IDevolucaoPendenteView>(viewModel), ITabNotaPendente {
  override val label: String
    get() = situacao()?.title ?: ""

  override val situacaoPendencia: ESituacaoPendencia?
    get() = situacao()

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci ?: return false
    return username.forPendente
  }

  init {

  }
}

