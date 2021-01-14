package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaNaoAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaViewModelNaoAgendada
import br.com.astrosoft.framework.model.IUser

class TabAgendaNaoAgendada(viewModel: TabAgendaViewModelNaoAgendada): TabAgendaAbstract(viewModel),
                                                                      ITabAgendaNaoAgendada {
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.agendaNaoAgendada == true
  }
  
  override val label: String
    get() = "Pré-entrada"
}
