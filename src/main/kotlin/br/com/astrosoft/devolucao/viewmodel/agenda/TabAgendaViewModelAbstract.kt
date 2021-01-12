package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.AgendaUpdate
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate
import br.com.astrosoft.framework.viewmodel.fail

abstract class TabAgendaViewModelAbstract(val viewModel: AgendaViewModel): IViewModelUpdate {
  protected abstract val subView: ITabAgenda
  
  override fun updateView() = viewModel.exec {
    subView.updateGrid(listAgenda(subView.agendado, subView.recebido))
  }
  
  private fun listAgenda(agendado: Boolean, recebido: Boolean) = Agenda.listaAgenda(agendado, recebido)
  fun salvaAgendamento(bean: AgendaUpdate?) = viewModel.exec {
    bean ?: fail("Agendamento inválido")
    bean.save()
    updateView()
  }
}

interface ITabAgenda: ITabView {
  fun updateGrid(itens: List<Agenda>)
  
  val agendado: Boolean
  val recebido: Boolean
}