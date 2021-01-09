package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaVMAgendada(viewModel: AgendaViewModel): TabAgendaVMAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabAgendaAgendada
  
}

interface ITabAgendaAgendada: ITabAgenda {
  override val agendado: Boolean
    get() = true
}