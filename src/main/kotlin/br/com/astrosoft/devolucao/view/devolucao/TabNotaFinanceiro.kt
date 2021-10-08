package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaFinanceiro
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaFinanceiroViewModel
import br.com.astrosoft.framework.model.IUser
import com.vaadin.flow.component.dependency.CssImport

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabNotaFinanceiro(viewModel: TabNotaFinanceiroViewModel) : TabDevolucaoAbstract<IDevolucao01View>(viewModel),
        ITabNotaFinanceiro {
  override val label: String
    get() = "Financeiro"
  override val situacaoPendencia: String?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.notaFinanceiro == true
  }
}