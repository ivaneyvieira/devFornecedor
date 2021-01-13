package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.AgendaUpdate
import br.com.astrosoft.devolucao.model.beans.NotaSaida
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
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.bind
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.getAll
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.provider.SortDirection.ASCENDING

abstract class TabAgendaAbstract(val viewModel: TabAgendaViewModelAbstract): TabPanelGrid<Agenda>(Agenda::class),
                                                                             ITabAgenda {
  override fun HorizontalLayout.toolBarConfig() { //Falta definir o filtro
  }
  
  override fun Grid<Agenda>.gridPanel() {
    addColumnButton(EDIT, "Agendamento", "Agd") {agenda ->
      DlgAgendamento(viewModel).edtAgendamento(agenda)
    }
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
    agendaTotal().let {col ->
      val lista = this.dataProvider.getAll()
      val total = lista.sumByDouble {it.total}.format()
      col.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${total}</font></b>"))
    }
    agendaTransp()
    agendaNome()
    agendaPedido()
  
    sort(listOf(GridSortOrder(getColumnBy(Agenda::data), ASCENDING), GridSortOrder(getColumnBy(Agenda::hora),
                                                                                   ASCENDING)))
  }
  
  override fun updateComponent() {
    viewModel.updateView()
  }
}

class DlgAgendamento(val viewModel: TabAgendaViewModelAbstract): VerticalLayout() {
  private val binder = Binder(AgendaUpdate::class.java)
  
  init {
    datePicker("Data Agendada") {
      bind(binder).bind(AgendaUpdate::data)
    }
    textField("Horário") {
      bind(binder).bind(AgendaUpdate::hora)
    }
    textField("Recebedor") {
      bind(binder).bind(AgendaUpdate::recebedor)
    }
  }
  
  fun edtAgendamento(agenda: Agenda) {
    val form = SubWindowForm("Nr. Ordem ${agenda.invno}  NF ${agenda.nf}", ::toolBar) {
      binder.bean = agenda.agendaUpdate()
      this
    }
    form.open()
  }
  
  private fun toolBar(hasComponents: HasComponents, subWindowForm: SubWindowForm) {
    hasComponents.apply {
      this.button("Salvar") {
        this.addThemeVariants(LUMO_PRIMARY)
        onLeftClick {
          viewModel.salvaAgendamento(binder.bean)
          subWindowForm.close()
        }
      }
    }
  }
}
