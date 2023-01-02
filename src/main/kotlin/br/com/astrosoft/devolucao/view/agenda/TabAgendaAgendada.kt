package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaAgendadaViewModel
import br.com.astrosoft.framework.model.IUser

class TabAgendaAgendada(viewModel: TabAgendaAgendadaViewModel) : TabAgendaAbstract(viewModel), ITabAgendaAgendada {
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.agendaAgendada == true
  }

  override val label: String
    get() = "Agendado"
}
