package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabMonitoramentoEntrada
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabMonitoramentoEntradaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.apache.poi.ss.formula.functions.T
import java.time.LocalDate

class TabMonitoramentoEntrada(private val viewModel: TabMonitoramentoEntradaViewModel) :
  TabPanelGrid<MonitoramentoEntradaFornecedor>(MonitoramentoEntradaFornecedor::class), ITabMonitoramentoEntrada {
  private var dlgNota: DlgNotaEntrada? = null
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

  override fun Grid<MonitoramentoEntradaFornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnSeq("Item")

    addColumnButton(iconButton = VaadinIcon.MODAL_LIST, tooltip = "Nota", header = "Nota") { monitoramento: MonitoramentoEntradaFornecedor ->
      dlgNota = DlgNotaEntrada(viewModel)
      dlgNota?.showDialogNota(monitoramento) {
        viewModel.updateView()
      }
    }

    columnGrid(MonitoramentoEntradaFornecedor::vendno, "Cod Forn")
    columnGrid(MonitoramentoEntradaFornecedor::custno, "Cliente")
    columnGrid(MonitoramentoEntradaFornecedor::fornecedor, "Fornecedor")
    columnGrid(MonitoramentoEntradaFornecedor::valorTotal, "Valor Total")
  }

  override fun filtro(): FiltroMonitoramentoEntrada {
    return FiltroMonitoramentoEntrada(
      pesquisa = edtQuery.value ?: "",
      dataInicial = edtDataInicial.value,
      dataFinal = edtDataFinal.value,
      loja = 0
    )
  }

  override fun imprimeSelecionados(notas: List<MonitoramentoEntradaNota>) {
    dlgNota?.imprimeSelecionados(notas)
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
