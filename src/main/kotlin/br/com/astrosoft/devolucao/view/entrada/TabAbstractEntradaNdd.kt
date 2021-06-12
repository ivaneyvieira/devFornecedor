package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.devolucao.model.beans.NotaEntradaNdd
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorNdd
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorNddResumido
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorCodigoSaci
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorSaldoTotal
import br.com.astrosoft.devolucao.view.entrada.columms.FornecedorNddViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNotaSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaTotal
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabAbstractEntradaNddViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabAbstractEntradaNddViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.*
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class TabAbstractEntradaNdd<T : ITabAbstractEntradaNddViewModel>(val viewModel: TabAbstractEntradaNddViewModel<T>) :
        TabPanelGrid<FornecedorNdd>(FornecedorNdd::class), ITabAbstractEntradaNddViewModel {
  private lateinit var edtFiltro: TextField
  abstract override val label: String

  override fun query(): String {
    return if (this::edtFiltro.isInitialized) edtFiltro.value ?: ""
    else ""
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

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaNdd == true
  }

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = ValueChangeMode.TIMEOUT
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

  private fun buttonPlanilha(notas: () -> List<FornecedorNdd>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FontAwesome.Solid.FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilha(notas()))
    }
  }

  private fun buttonPlanilhaResumo(notas: () -> List<FornecedorNdd>): LazyDownloadButton {
    return LazyDownloadButton("Planilha Resumo", FontAwesome.Solid.FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilhaResumo(notas()))
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
    val totalCol = fornecedorSaldoTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(FornecedorNdd::nome), SortDirection.ASCENDING)))
  }
}

class DlgNotaPainelNddSaci(val viewModel: TabAbstractEntradaNddViewModel<*>) {
  fun showDialogNota(fornecedor: FornecedorNdd?) {
    fornecedor ?: return

    val listNotasNdd = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridNota = createGridNdd(listNotasNdd)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGridNdd(listParcelas: List<NotaEntradaNdd>): Grid<NotaEntradaNdd> {
    return Grid(NotaEntradaNdd::class.java, false).apply<Grid<NotaEntradaNdd>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(MULTI)
      setItems(listParcelas)

      notaLoja()
      notaNotaSaci()
      notaData()
      notaTotal().apply {
        val totalPedido = listParcelas.sumOf { it.baseCalculoIcms }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      //sort(listOf(GridSortOrder(getColumnBy(NotaDevolucaoSap::nfSaci), SortDirection.ASCENDING)))

      listParcelas.forEach { parcela ->
        setDetailsVisible(parcela, true)
      }
    }
  }
}