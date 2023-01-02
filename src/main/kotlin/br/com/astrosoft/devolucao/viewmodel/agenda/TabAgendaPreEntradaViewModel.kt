package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaPreEntradaViewModel(viewModel: AgendaViewModel) : TabAgendaViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabAgendaPreEntrada
}

interface ITabAgendaPreEntrada : ITabAgenda {
  override val agendado: Boolean
    get() = false
  override val recebido: Boolean
    get() = false
}