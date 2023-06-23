package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01Coleta
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01ColetaViewModel
import br.com.astrosoft.framework.model.IUser

class TabNotaSerie01Coleta(viewModel: TabNotaSerie01ColetaViewModel) :
    TabDevolucaoAbstract<IDevolucao01View>(viewModel), ITabNotaSerie01Coleta {
  override val label: String
    get() = "Notas Coleta"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return true
  }
}