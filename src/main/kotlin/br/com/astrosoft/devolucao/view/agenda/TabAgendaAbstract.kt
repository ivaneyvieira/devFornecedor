package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.AgendaUpdate
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaAbrev
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaData
import br.com.astrosoft.devolucao.view.agenda.columns.AgendaViewColumns.agendaDataHoraRecebedor
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
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

abstract class TabAgendaAbstract(val viewModel: TabAgendaViewModelAbstract) : TabPanelGrid<Agenda>(Agenda::class),
        ITabAgenda {
  private lateinit var edtFiltro: TextField

  override fun HorizontalLayout.toolBarConfig() { //Falta definir o filtro
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun filtro() = edtFiltro.value ?: ""

  override fun Grid<Agenda>.gridPanel() {
    addColumnButton(EDIT, "Agendamento", "Agd") { agenda ->
      DlgAgendamento(viewModel).edtAgendamento(agenda)
    }
    agendaLoja()
    agendaData()

    agendaHora()
    agendaRecebedor()
    agendaDataHoraRecebedor()
    agendaOrd()

    agendaPedido()
    agendaFornecedor()
    agendaAbrev()

    agendaEmissao()
    agendaNf()
    agendaTransp()

    agendaNome()
    agendaVolume()
    agendaTotal().let { col ->
      this.dataProvider.addDataProviderListener {
        val lista = this.dataProvider.getAll()
        val total = lista.sumOf { it.total }.format()
        col.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${total}</font></b>"))
      }
    }

    sort(listOf(GridSortOrder(getColumnBy(Agenda::data), ASCENDING),
                GridSortOrder(getColumnBy(Agenda::hora), ASCENDING)))
  }

  override fun updateComponent() {
    viewModel.updateView()
  }
}

class DlgAgendamento(val viewModel: TabAgendaViewModelAbstract) : VerticalLayout() {
  private val binder = Binder(AgendaUpdate::class.java)

  init {
    datePicker("Data Agendada") {
      this.isClearButtonVisible = true
      this.isAutoOpen = true
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
