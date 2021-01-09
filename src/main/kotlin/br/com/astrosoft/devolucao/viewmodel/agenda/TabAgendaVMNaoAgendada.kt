package br.com.astrosoft.devolucao.viewmodel.agenda

class TabAgendaVMNaoAgendada(viewModel: AgendaViewModel): TabAgendaVMAbstract(viewModel)  {
  override val subView
    get() = viewModel.view.tabAgendaNaoAgendada
}

interface ITabAgendaNaoAgendada: ITabAgenda {
  override val agendado: Boolean
    get() = false

}