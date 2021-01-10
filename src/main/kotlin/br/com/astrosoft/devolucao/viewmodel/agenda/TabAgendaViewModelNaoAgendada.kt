package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaViewModelNaoAgendada(viewModel: AgendaViewModel): TabAgendaViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabAgendaNaoAgendada
}

interface ITabAgendaNaoAgendada: ITabAgenda {
  override val agendado: Boolean
    get() = false
}