package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.agenda.AgendaViewModel
import br.com.astrosoft.devolucao.viewmodel.agenda.IAgendaView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Agenda")
@CssImport("./styles/gridTotal.css")
class AgendaView: ViewLayout<AgendaViewModel>(), IAgendaView {
  override val viewModel: AgendaViewModel = AgendaViewModel(this)
  
  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuAgenda
  }
  
  override val tabAgendaAgendada = TabAgendaAgendada(viewModel.tabAgendadaVMAgendada)
  override val tabAgendaNaoAgendada = TabAgendaNaoAgendada(viewModel.tabAgendadaVMNaoAgendada)
  override val tabAgendaRecebida = TabAgendaRecebida(viewModel.tabAgendadaVMRecebida)
  
  init {
    addTabSheat(viewModel)
  }
}