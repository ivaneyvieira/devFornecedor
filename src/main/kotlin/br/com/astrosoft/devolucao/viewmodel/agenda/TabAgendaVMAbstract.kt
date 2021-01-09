package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.framework.viewmodel.ITabView

abstract class TabAgendaVMAbstract(val viewModel: AgendaViewModel) {
  protected abstract val subView: ITabAgenda
  
  fun updateGridNota() = viewModel.exec {
    subView.updateGrid(listAgenda(subView.agendado))
  }
  
  private fun listAgenda(agendado: Boolean) = Agenda.listaAgenda(agendado)
}

interface ITabAgenda : ITabView {
  fun updateGrid(listAgenda: List<Agenda>)
  
  val agendado: Boolean
}