package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.view.agendaAbrev
import br.com.astrosoft.devolucao.view.agendaData
import br.com.astrosoft.devolucao.view.agendaEmissao
import br.com.astrosoft.devolucao.view.agendaFornecedor
import br.com.astrosoft.devolucao.view.agendaHora
import br.com.astrosoft.devolucao.view.agendaLoja
import br.com.astrosoft.devolucao.view.agendaNf
import br.com.astrosoft.devolucao.view.agendaNome
import br.com.astrosoft.devolucao.view.agendaOrd
import br.com.astrosoft.devolucao.view.agendaPedido
import br.com.astrosoft.devolucao.view.agendaRecebedor
import br.com.astrosoft.devolucao.view.agendaTotal
import br.com.astrosoft.devolucao.view.agendaTransp
import br.com.astrosoft.devolucao.view.agendaVolume
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgenda
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaVMAbstract
import br.com.astrosoft.framework.view.TabPanelGrid
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

abstract class TabAgendaAbstract(val viewModel: TabAgendaVMAbstract):
  TabPanelGrid<Agenda>(Agenda::class), ITabAgenda {
  override fun HorizontalLayout.toolBarConfig() {
    //Falta definir o filtro
  }
  
  override fun Grid<Agenda>.gridPanel()
  {
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
    viewModel.updateGridNota()
  }
}