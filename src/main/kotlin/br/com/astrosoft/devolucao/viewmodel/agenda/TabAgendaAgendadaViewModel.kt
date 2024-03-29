package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaAgendadaViewModel(viewModel: AgendaViewModel) : TabAgendaViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabAgendaAgendada
}

interface ITabAgendaAgendada : ITabAgenda {
  override val agendado: Boolean
    get() = true
  override val recebido: Boolean
    get() = false
}