package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class AgendaViewModel(view: IAgendaView): ViewModel<IAgendaView>(view) {
  
  val tabAgendadaVMAgendada = TabAgendaVMAgendada(this)
  val tabAgendadaVMNaoAgendada = TabAgendaVMNaoAgendada(this)
  
  private val listTab: List<ITabView>
    get() = listOf(view.tabAgendaAgendada,
                   view.tabAgendaNaoAgendada
                  )
  
  fun tabsAuthorized() = listTab.filter {
    it.isAuthorized()
  }
  
}

interface IAgendaView: IView {
  val tabAgendaAgendada: ITabAgendaAgendada
  val tabAgendaNaoAgendada: ITabAgendaNaoAgendada
}