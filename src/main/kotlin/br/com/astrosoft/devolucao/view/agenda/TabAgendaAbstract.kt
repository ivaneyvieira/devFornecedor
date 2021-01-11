package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaAbrev
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaData
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaEmissao
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaFornecedor
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaHora
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaLoja
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaNf
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaNome
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaOrd
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaPedido
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaRecebedor
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaTotal
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaTransp
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaVolume
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgenda
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaViewModelAbstract
import br.com.astrosoft.framework.view.TabPanelGrid
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

abstract class TabAgendaAbstract(val viewModel: TabAgendaViewModelAbstract): TabPanelGrid<Agenda>(Agenda::class),
                                                                             ITabAgenda {
  override fun HorizontalLayout.toolBarConfig() { //Falta definir o filtro
  }
  
  override fun Grid<Agenda>.gridPanel() {
    agendaLoja()
    agendaData()
    agendaHora()
    agendaRecebedor()
    agendaOrd()
    agendaFornecedor()
    agendaAbrev()
    agendaEmissao()
    agendaNf()
    agendaVolume()
    agendaTotal()
    agendaTransp()
    agendaNome()
    agendaPedido()
  }
  
  override fun updateComponent() {
    viewModel.updateView()
  }
}