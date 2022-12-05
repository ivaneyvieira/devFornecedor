package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaAbrev
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaCnpj
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaColeta
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaCte
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaData
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaDataHoraRecebedor
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaEmissao
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaFornecedor
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaFrete
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
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaNaoAgendada
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaViewModelNaoAgendada
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.kaributools.fetchAll
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

class TabAgendaPreEntrada(viewModel: TabAgendaViewModelNaoAgendada) : TabAgendaAbstract(viewModel),
        ITabAgendaNaoAgendada {
  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.agendaNaoAgendada == true
  }

  override val label: String
    get() = "Pré-entrada"

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
