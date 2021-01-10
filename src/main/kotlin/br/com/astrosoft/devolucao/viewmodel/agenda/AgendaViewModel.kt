package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class AgendaViewModel(view: IAgendaView): ViewModel<IAgendaView>(view) {
  val tabAgendadaVMAgendada = TabAgendaViewModelAgendada(this)
  val tabAgendadaVMNaoAgendada = TabAgendaViewModelNaoAgendada(this)
  
  override fun listTab() = listOf(view.tabAgendaAgendada, view.tabAgendaNaoAgendada)
}

interface IAgendaView: IView {
  val tabAgendaAgendada: ITabAgendaAgendada
  val tabAgendaNaoAgendada: ITabAgendaNaoAgendada
}