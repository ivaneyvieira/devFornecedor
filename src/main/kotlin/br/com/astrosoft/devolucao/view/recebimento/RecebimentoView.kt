package br.com.astrosoft.devolucao.view.recebimento

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.recebimento.IRecebimentoView
import br.com.astrosoft.devolucao.viewmodel.recebimento.RecebimentoViewModel
import br.com.astrosoft.framework.view.ITabPanel
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Recebimento")
@CssImport("./styles/gridTotal.css")
class RecebimentoView: ViewLayout<RecebimentoViewModel>(), IRecebimentoView {
  override val viewModel = RecebimentoViewModel(this)
  override val tabNotaPendente = TabNotaPendente(viewModel.tabNotaPendenteViewModel)
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    tabSheet {
      setSizeFull()
      val tabs = viewModel.tabsAuthorized()
      tabs.forEach {tab ->
        tabPanel(tab as ITabPanel)
      }
      tabs.firstOrNull()
        ?.updateComponent()
    }
  }
}