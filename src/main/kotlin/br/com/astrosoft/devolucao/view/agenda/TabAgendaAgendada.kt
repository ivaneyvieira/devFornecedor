package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaVMAgendada

class TabAgendaAgendada(viewModel: TabAgendaVMAgendada) : TabAgendaAbstract(viewModel),
                                                          ITabAgendaAgendada {
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.agendaAgendada == true
  }
  
  override val label: String
    get() = "Agendado"
}
