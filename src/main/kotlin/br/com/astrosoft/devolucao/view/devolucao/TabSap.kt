package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedor
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorSap
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorSapResumido
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorCodigoSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorCodigoSap
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorSaldoTotal
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapData
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapNotaSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapNumero
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabSap
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabSapViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.*
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.data.value.ValueChangeMode.*
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TabSap(val viewModel: TabSapViewModel) : TabPanelGrid<FornecedorSap>(FornecedorSap::class), ITabSap {
  private lateinit var edtFiltro: TextField

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = TIMEOUT
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
    this.add(buttonPlanilha {
      itensSelecionados()
    })
    this.add(buttonPlanilhaResumo {
      itensSelecionados()
    })
  }

  private fun buttonPlanilha(notas: () -> List<FornecedorSap>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilha(notas()))
    }
  }

  private fun buttonPlanilhaResumo(notas: () -> List<FornecedorSap>): LazyDownloadButton {
    return LazyDownloadButton("Planilha Resumo", FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilhaResumo(notas()))
    }
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  override fun Grid<FornecedorSap>.gridPanel() {
    setSelectionMode(MULTI)
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNotaPainelSapSaci(viewModel).showDialogNota(fornecedor)
    }

    fornecedorCodigoSaci()
    fornecedorCodigoSap()
    fornecedorNome()
    fornecedorPrimeiraData()
    fornecedorUltimaData()
    val totalCol = fornecedorSaldoTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.saldoTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(FornecedorSap::nome), SortDirection.ASCENDING)))
  }

  override fun filtro(): String {
    return if (this::edtFiltro.isInitialized) edtFiltro.value ?: ""
    else ""
  }

  override fun imprimeRelatorio(fornecedores: List<FornecedorSap>) {
    val report = RelatorioFornecedorSap.processaRelatorioFornecedor(fornecedores)
    val chave = "DevFornecedorSap"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimeRelatorioResumido(fornecedores: List<FornecedorSap>) {
    val report = RelatorioFornecedorSapResumido.processaRelatorio(fornecedores)
    val chave = "DevFornecedorSapResumo"
    SubWindowPDF(chave, report).open()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.sap == true
  }

  override val label: String
    get() = "Sap"

  override fun updateComponent() {
    viewModel.updateView()
  }
}

class DlgNotaPainelSapSaci(val viewModel: TabSapViewModel) {
  fun showDialogNota(fornecedor: FornecedorSap?) {
    fornecedor ?: return

    val listNotasSap = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridNota = createGridSap(listNotasSap)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGridSap(listParcelas: List<NotaDevolucaoSap>): Grid<NotaDevolucaoSap> {
    val gridDetail = Grid(NotaDevolucaoSap::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(MULTI)
      setItems(listParcelas)

      notaSapLoja()
      notaSapNumero()
      notaSapNotaSaci()
      notaSapData()
      notaSapTotal().apply {
        val totalPedido = listParcelas.sumOf { it.saldo }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      //sort(listOf(GridSortOrder(getColumnBy(NotaDevolucaoSap::nfSaci), SortDirection.ASCENDING)))

      listParcelas.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }
    }
    return grid
  }
}

