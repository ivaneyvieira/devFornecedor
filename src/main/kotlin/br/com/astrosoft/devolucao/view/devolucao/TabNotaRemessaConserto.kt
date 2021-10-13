package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaRemessaConserto
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaRemessaConsertoViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaRemessaConserto(viewModel: TabNotaRemessaConsertoViewModel) :
        TabDevolucaoAbstract<IDevolucao01View>(viewModel), ITabNotaRemessaConserto {
  override val label: String
    get() = "Notas Conserto"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.remessaConserto == true
  }
}