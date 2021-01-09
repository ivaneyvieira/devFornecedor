package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.agenda.AgendaViewModel
import br.com.astrosoft.devolucao.viewmodel.agenda.IAgendaView
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaNaoAgendada
import br.com.astrosoft.framework.view.ITabPanel
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Agenda")
@CssImport("./styles/gridTotal.css")
class AgendaView : ViewLayout<AgendaViewModel>(), IAgendaView {
  override val viewModel: AgendaViewModel = AgendaViewModel(this)
  
  override fun isAccept(user: UserSaci) = true
  
  
  override val tabAgendaAgendada = TabAgendaAgendada(viewModel.tabAgendadaVMAgendada)
  override val tabAgendaNaoAgendada = TabAgendaNaoAgendada(viewModel.tabAgendadaVMNaoAgendada)
  
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