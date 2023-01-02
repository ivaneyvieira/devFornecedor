package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class AgendaViewModel(view: IAgendaView) : ViewModel<IAgendaView>(view) {
  val tabAgendadaVMAgendada = TabAgendaViewModelAgendada(this)
  val tabAgendadaVMNaoAgendada = TabAgendaViewModelNaoAgendada(this)
  val tabAgendadaVMRecebida = TabAgendaViewModelRecebida(this)
  val tabAgendadaRastreamento = TabAgendaRastreamentoViewModel(this)

  override fun listTab() =
    listOf(view.tabAgendaNaoAgendada, view.tabAgendaRastreamento, view.tabAgendaAgendada, view.tabAgendaRecebida)
}

interface IAgendaView : IView {
  val tabAgendaRecebida: ITabAgendaRecebida
  val tabAgendaAgendada: ITabAgendaAgendada
  val tabAgendaNaoAgendada: ITabAgendaNaoAgendada
  val tabAgendaRastreamento: ITabAgendaRastreamento
}