package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.view.agenda.TabAgendaAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.AgendaViewModel
import br.com.astrosoft.devolucao.viewmodel.demanda.DemandaViewModel
import br.com.astrosoft.devolucao.viewmodel.demanda.IDemandaView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Demanda")
@CssImport("./styles/gridTotal.css")
class DemandaView : ViewLayout<DemandaViewModel>(), IDemandaView {
  override val viewModel = DemandaViewModel(this)
  override val tabAgendaDemanda = TabAgendaDemanda(viewModel.tabAgendadaDemanda)
  override val tabConcluidoDemanda = TabConcluidoDemanda(viewModel.tabConcluidoDemanda)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDemanda
  }

  init {
    addTabSheat(viewModel)
  }
}