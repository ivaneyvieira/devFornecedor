package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaRastreamentoViewModel(viewModel: AgendaViewModel) : TabAgendaViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabAgendaRastreamento
}

interface ITabAgendaRastreamento : ITabAgenda {
  override val agendado: Boolean
    get() = false
  override val recebido: Boolean
    get() = false
}