package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.view.devFornecedor.TabFornecedorAbstract
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaNaoAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaVMNaoAgendada
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01

class TabAgendaNaoAgendada(viewModel: TabAgendaVMNaoAgendada) : TabAgendaAbstract(viewModel),
                                                                               ITabAgendaNaoAgendada {
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.agendaNaoAgendada == true
  }
  
  override val label: String
    get() = "Não agendado"
}
