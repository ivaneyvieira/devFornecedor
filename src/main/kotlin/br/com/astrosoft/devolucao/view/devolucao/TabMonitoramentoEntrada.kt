package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.ContaRazao
import br.com.astrosoft.devolucao.model.beans.FiltroMonitoramentoEntrada
import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntrada
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaContaRazao
import br.com.astrosoft.devolucao.view.demanda.DlgContaRazaoNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabMonitoramentoEntrada
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabMonitoramentoEntradaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.asc
import com.github.mvysny.kaributools.getColumnBy
import com.github.mvysny.kaributools.sort
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDate

class TabMonitoramentoEntrada(private val viewModel: TabMonitoramentoEntradaViewModel) :
  TabPanelGrid<MonitoramentoEntrada>(MonitoramentoEntrada::class), ITabMonitoramentoEntrada {
  private lateinit var edtQuery: TextField
  private lateinit var edtDataInicial: DatePicker
  private lateinit var edtDataFinal: DatePicker

  override fun HorizontalLayout.toolBarConfig() {
    edtQuery = textField("Filtro") {
      width = "300px"
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    edtDataInicial = datePicker("Data Inicial") {
      value = LocalDate.now()
      this.localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    edtDataFinal = datePicker("Data Final") {
      value = LocalDate.now()
      this.localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<MonitoramentoEntrada>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnSeq("Item")

    /*
    addColumnButton(iconButton = VaadinIcon.MODAL_LIST, tooltip = "Nota", header = "Nota") { monitoramento ->
      viewModel.showNotas(monitoramento)
    }*/

    columnGrid(MonitoramentoEntrada::loja, "Loja")
    columnGrid(MonitoramentoEntrada::ni, "NI")
    columnGrid(MonitoramentoEntrada::dataEntrada, "Data Entrada")
    columnGrid(MonitoramentoEntrada::vendno, "Cod Forn")
    columnGrid(MonitoramentoEntrada::fornecedor, "Fornecedor")
    columnGrid(MonitoramentoEntrada::codigo, "Código")
    columnGrid(MonitoramentoEntrada::grade, "Grade")
    columnGrid(MonitoramentoEntrada::descricao, "Descrição")
    columnGrid(MonitoramentoEntrada::observacao, "Observação")
  }

  override fun filtro(): FiltroMonitoramentoEntrada {
    return FiltroMonitoramentoEntrada(
      pesquisa = edtQuery.value ?: "",
      dataInicial = edtDataInicial.value,
      dataFinal = edtDataFinal.value,
      loja = 0
    )
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoMonitoramentoEntrada == true
  }

  override val label: String
    get() = "Produtos Entrada"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
