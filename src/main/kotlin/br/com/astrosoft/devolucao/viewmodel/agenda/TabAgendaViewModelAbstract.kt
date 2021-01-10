package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate

abstract class TabAgendaViewModelAbstract(val viewModel: AgendaViewModel) : IViewModelUpdate {
  protected abstract val subView: ITabAgenda
  
  override fun updateView() = viewModel.exec {
    subView.updateGrid(listAgenda(subView.agendado))
  }
  
  private fun listAgenda(agendado: Boolean) = Agenda.listaAgenda(agendado)
}

interface ITabAgenda: ITabView {
  fun updateGrid(itens: List<Agenda>)
  
  val agendado: Boolean
}