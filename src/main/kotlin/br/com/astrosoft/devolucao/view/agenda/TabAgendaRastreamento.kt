package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaAbrev
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaCnpj
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaColeta
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaCte
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaData
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaEmissao
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaFornecedor
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaFrete
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaHora
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaLoja
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaNf
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaNome
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaOrd
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaPedido
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaTransp
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaVolume
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaRastreamento
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaRastreamentoViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.addColumnButton
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon

class TabAgendaRastreamento(viewModel: TabAgendaRastreamentoViewModel) : TabAgendaAbstract(viewModel),
  ITabAgendaRastreamento {
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.agendaRastreamento == true
  }

  override val label: String
    get() = "Rastremento"

  override fun Grid<Agenda>.colunasGrid() {
    addColumnButton(VaadinIcon.EDIT, "Agendamento", "Agd") { agenda ->
      DlgAgendamento(viewModel).edtAgendamento(agenda)
    }
    agendaLoja()
    agendaColeta()
    agendaCte()
    agendaData()
    agendaHora()
    agendaFrete()
    agendaEmissao()
    agendaNf()
    agendaTransp()

    agendaNome()
    agendaCnpj()
    agendaVolume()
    agendaOrd()

    agendaPedido()
    agendaFornecedor()
    agendaAbrev()
  }
}
