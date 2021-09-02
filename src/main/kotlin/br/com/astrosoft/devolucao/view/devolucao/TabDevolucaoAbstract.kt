package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.beans.Loja.Companion.lojaZero
import br.com.astrosoft.devolucao.model.reports.*
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorValorTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
abstract class TabDevolucaoAbstract<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) :
        TabPanelGrid<Fornecedor>(Fornecedor::class), ITabNota {
  private lateinit var edtFiltro: TextField
  private lateinit var cmbLoja: ComboBox<Loja>

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "300px"
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        viewModel.updateFiltro()
      }
    }
    cmbLoja = comboBox("Loja") {
      val lojas: List<Loja> = Loja.allLojas() + lojaZero
      setItems(lojas.sortedBy { it.no })
      setItemLabelGenerator { loja ->
        val lojaValue = loja ?: lojaZero
        "${lojaValue.no} - ${lojaValue.sname}"
      }
      isAllowCustomValue = false
      isClearButtonVisible = true
      value = lojaZero
      isVisible = serie == PED
      addValueChangeListener {
        viewModel.updateFiltro()
      }
    }
    button("Relatório") {
      icon = PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorioFornecedor(fornecedores.flatMap { it.notas })
      }
    }
    button("Relatório Resumido") {
      icon = PRINT.create()
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

  private fun buttonPlanilha(fornecedores: () -> List<Fornecedor>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FILE_EXCEL.create(), ::filename) {
      val notas = fornecedores().flatMap { it.notas }
      ByteArrayInputStream(viewModel.geraPlanilha(notas, serie))
    }
  }

  private fun buttonPlanilhaResumo(fornecedores: () -> List<Fornecedor>): LazyDownloadButton {
    return LazyDownloadButton("Planilha Resumo", FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilhaResumo(fornecedores()))
    }
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  override fun Grid<Fornecedor>.gridPanel() {
    setSelectionMode(MULTI)
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNota(viewModel).showDialogNota(fornecedor, serie){
        viewModel.updateView()
      }
    }
    addColumnButton(MONEY, "Parcelas do fornecedor", "Parcelas") { fornecedor ->
      DlgParcelas(viewModel).showDialogParcela(fornecedor, serie)
    }
    addColumnButton(EDIT, "Editor", "Edt", ::configIconEdt) { fornecedor ->
      viewModel.editRmkVend(fornecedor)
    }
    addColumnButton(PHONE_LANDLINE, "Representantes", "Rep") { fornecedor ->
      DlgFornecedor().showDialogRepresentante(fornecedor)
    }
    if (serie != ENT) fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()

    if (serie in listOf(FIN, Serie01, PED)) {
      chaveDesconto()
    }
    else {
      fornecedorPrimeiraData()
      fornecedorUltimaData()
    }

    val totalCol = fornecedorValorTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(Fornecedor::fornecedor), SortDirection.ASCENDING)))
  }

  override fun editRmkVend(fornecedor: Fornecedor, save: (Fornecedor) -> Unit) {
    DlgEditRmkVend().editRmk(fornecedor, save)
  }

  private fun configIconEdt(icon: Icon, fornecedor: Fornecedor) {
    if (fornecedor.obs.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  override fun filtro() = FiltroFornecedor(query = edtFiltro.value ?: "", loja = cmbLoja.value ?: lojaZero)

  override fun setFiltro(filtro: FiltroFornecedor) {
    edtFiltro.value = filtro.query
    cmbLoja.value = filtro.loja
  }

  override fun imprimeSelecionados(notas: List<NotaSaida>, resumida: Boolean) {
    val report = RelatorioNotaDevolucao.processaRelatorio(notas, resumida)
    val chave = "DevReport"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimeNotaFornecedor(notas: List<NotaSaida>, ocorrencias: List<String>) {
    val report = RelatorioNotaDevFornecedor.processaRelatorio(notas, ocorrencias)
    val chave = "DevReportVend"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimirRelatorioFornecedor(notas: List<NotaSaida>) {
    val report = RelatorioFornecedor.processaRelatorio(notas)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimirRelatorio(notas: List<NotaSaida>) {
    val report = RelatorioNotaFornecedor.processaRelatorio(notas)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimirRelatorioResumido(fornecedores: List<Fornecedor>) {
    val report = RelatorioFornecedorResumido.processaRelatorio(fornecedores)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
  }

  override fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    DlgEditRmk().editRmk(nota, save)
  }

  override fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit) {
    DlgEditFile(viewModel).editFile(nota, insert)
  }

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun enviaEmail(notas: List<NotaSaida>) {
    DlgEnviaEmail(viewModel).enviaEmail(notas)
  }

  override fun selecionaEmail(nota: NotaSaida, emails: List<EmailDB>) {
    DlgSelecionaEmail(viewModel).selecionaEmail(nota, emails)
  }
}

