package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class AgendaViewModel(view: IAgendaView) : ViewModel<IAgendaView>(view) {
  val tabAgendadaAgendada = TabAgendaAgendadaViewModel(this)
  val tabAgendadaPreEntrada = TabAgendaPreEntradaViewModel(this)
  val tabAgendadaRecebida = TabAgendaRecebidaViewModel(this)
  val tabAgendadaRastreamento = TabAgendaRastreamentoViewModel(this)

  override fun listTab() =
    listOf(view.tabAgendaPreEntrada, view.tabAgendaRastreamento, view.tabAgendaAgendada, view.tabAgendaRecebida)
}

interface IAgendaView : IView {
  val tabAgendaRecebida: ITabAgendaRecebida
  val tabAgendaAgendada: ITabAgendaAgendada
  val tabAgendaPreEntrada: ITabAgendaPreEntrada
  val tabAgendaRastreamento: ITabAgendaRastreamento
}