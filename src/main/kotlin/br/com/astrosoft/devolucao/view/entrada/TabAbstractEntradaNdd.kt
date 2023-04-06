package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorNdd
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorNddResumido
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorCodigoSaci
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorSaldoTotal
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorTemIPI
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.viewmodel.entrada.ETemIPI
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabAbstractEntradaNddViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabAbstractEntradaNddViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class TabAbstractEntradaNdd<T : ITabAbstractEntradaNddViewModel>(val viewModel: TabAbstractEntradaNddViewModel<T>) :
  TabPanelGrid<FornecedorNdd>(FornecedorNdd::class), ITabAbstractEntradaNddViewModel {
  private lateinit var edtFiltro: TextField
  private lateinit var edtDataInicial: DatePicker
  private lateinit var edtDataFinal: DatePicker
  private lateinit var cmbTemIPI: ComboBox<ETemIPI>
  abstract override val label: String

  override fun query(): String {
    return if (this::edtFiltro.isInitialized) edtFiltro.value ?: ""
    else ""
  }

  override fun dataInicial(): LocalDate? {
    return if (this::edtDataInicial.isInitialized) edtDataInicial.value
    else null
  }

  override fun temIPI(): ETemIPI? {
    return if (this::cmbTemIPI.isInitialized) cmbTemIPI.value
    else null
  }

  override fun dataFinal(): LocalDate? {
    return if (this::edtDataFinal.isInitialized) edtDataFinal.value
    else null
  }

  override fun imprimeRelatorio(fornecedores: List<FornecedorNdd>) {
    val report = RelatorioFornecedorNdd.processaRelatorioFornecedor(fornecedores)
    val chave = "DevFornecedorNdd"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimeRelatorioResumido(fornecedores: List<FornecedorNdd>) {
    val report = RelatorioFornecedorNddResumido.processaRelatorio(fornecedores)
    val chave = "DevFornecedorNddResumo"
    SubWindowPDF(chave, report).open()
  }

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    edtDataInicial = datePicker("Data Inicial") {
      value = LocalDate.now() //.minusMonths(6)
      isClearButtonVisible = true
      isAutoOpen = true
      localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    edtDataFinal = datePicker("Data Final") {
      value = LocalDate.now()
      isClearButtonVisible = true
      isAutoOpen = true
      localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    cmbTemIPI = comboBox("Tem IPI") {
      isAutoOpen = true
      this.isAllowCustomValue = false
      setItems(ETemIPI.values().toList())
      setItemLabelGenerator {
        it.descricao
      }
      value = ETemIPI.TODOS
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    button("Relatório") {
      icon = VaadinIcon.PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorio(fornecedores)
      }
    }
    button("Relatório Resumido") {
      icon = VaadinIcon.PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorioResumido(fornecedores)
      }
    }
    this.lazyDownloadButtonXlsx("Planilha", "planilha") {
      viewModel.geraPlanilha(itensSelecionados())
    }
    this.lazyDownloadButtonXlsx("Planilha", "planilhaResumo") {
      viewModel.geraPlanilhaResumo(itensSelecionados())
    }
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  override fun Grid<FornecedorNdd>.gridPanel() {
    setSelectionMode(MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNotaPainelNddSaci(viewModel).showDialogNota(fornecedor)
    }

    fornecedorCodigoSaci()
    fornecedorNome()
    fornecedorPrimeiraData()
    fornecedorUltimaData()
    fornecedorTemIPI()
    val totalCol = fornecedorSaldoTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(FornecedorNdd::nome), SortDirection.ASCENDING)))
  }
}

