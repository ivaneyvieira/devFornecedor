package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaViewModelRecebida(viewModel: AgendaViewModel): TabAgendaViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabAgendaRecebida
}

interface ITabAgendaRecebida: ITabAgenda {
  override val agendado: Boolean
    get() = true
  override val recebido: Boolean
    get() = true
}