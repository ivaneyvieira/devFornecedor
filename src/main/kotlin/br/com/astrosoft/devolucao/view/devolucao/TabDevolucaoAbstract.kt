package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.beans.Loja.Companion.lojaZero
import br.com.astrosoft.devolucao.model.reports.*
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.dataSituacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.docSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorValorTotal
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.niSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.notaSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.observacaoChaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.tituloSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.getColumnBy
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
import com.vaadin.flow.data.value.ValueChangeMode.LAZY

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
abstract class TabDevolucaoAbstract<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) :
        TabPanelGrid<Fornecedor>(Fornecedor::class), ITabNota {
  protected var dlgNota: DlgNotaAbstract<T>? = null
  protected lateinit var dataSitCol: Grid.Column<Fornecedor>
  protected lateinit var userCol: Grid.Column<Fornecedor>
  protected lateinit var situacaoCol: Grid.Column<Fornecedor>
  protected lateinit var notaCol: Grid.Column<Fornecedor>
  protected lateinit var niCol: Grid.Column<Fornecedor>
  protected lateinit var dataCol: Grid.Column<Fornecedor>
  protected lateinit var tituloCol: Grid.Column<Fornecedor>
  protected lateinit var docCol: Grid.Column<Fornecedor>
  private lateinit var edtFiltro: TextField
  private lateinit var cmbLoja: ComboBox<Loja>

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "300px"
      valueChangeMode = LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
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
        viewModel.updateView()
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
    this.lazyDownloadButtonXlsx("Planilha", "planilha") {
      val notas = itensSelecionados().flatMap { it.notas }
      viewModel.geraPlanilha(notas, serie)
    }
    this.lazyDownloadButtonXlsx("Planilha Resumo", "planilhaResumo") {
      viewModel.geraPlanilhaResumo(itensSelecionados())
    }
  }

  override fun Grid<Fornecedor>.gridPanel() {
    setSelectionMode(MULTI)
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->
      dlgNota = DlgNota(viewModel)
      dlgNota?.showDialogNota(fornecedor, serie, situacaoPendencia) {
        viewModel.updateView()
      }
    }
    if (serie !in listOf(AJT, AJD, AJP, AJC, A66)) {
      addColumnButton(MONEY, "Parcelas do fornecedor", "Parcelas") { fornecedor ->
        DlgParcelas(viewModel).showDialogParcela(fornecedor, serie)
      }
    }
    addColumnButton(EDIT, "Editor", "Edt", ::configIconEdt) { fornecedor ->
      viewModel.editRmkVend(fornecedor)
    }
    if (serie !in listOf(AJT, AJD, AJP, AJC, A66)) {
      if (this@TabDevolucaoAbstract !is TabNotaPendente) {
        addColumnButton(PHONE_LANDLINE, "Representantes", "Rep") { fornecedor ->
          DlgFornecedor().showDialogRepresentante(fornecedor)
        }
      }
    }
    if (serie != ENT) fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()

    if (serie in listOf(FIN, Serie01, PED)) {
      if (serie in listOf(Serie01)) {
        fornecedorPrimeiraData()
      }
      if (this@TabDevolucaoAbstract is TabNotaPendente) {
        userCol = usuarioSituacao()
        dataSitCol = dataSituacaoDesconto().apply {
          this.isVisible = false
        }
        situacaoCol = situacaoDesconto()
        notaCol = notaSituacao()
        docCol = docSituacao()
        tituloCol = tituloSituacao()
        niCol = niSituacao()
      }
      else if (this@TabDevolucaoAbstract is TabPedidoPendente) {
        userCol = usuarioSituacao()
        situacaoCol = situacaoDesconto()
      }
      dataCol = dataAgendaDesconto()
      observacaoChaveDesconto()
    }
    else {
      fornecedorPrimeiraData()
      if (serie !in listOf(AJT, AJD, AJP, AJC, A66)) {
        fornecedorUltimaData()
      }
    }

    if (serie in listOf(AJT, AJD, AJP, AJC, A66)) {
      fornecedorObservacao()
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

  protected fun configIconEdt(icon: Icon, fornecedor: Fornecedor) {
    if (fornecedor.obs.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  override fun filtro() = FiltroFornecedor(query = edtFiltro.value ?: "", loja = cmbLoja.value ?: lojaZero)

  override fun setFiltro(filtro: FiltroFornecedor) {
    edtFiltro.value = filtro.query
    cmbLoja.value = filtro.loja
  }

  override fun imprimeSelecionados(notas: List<NotaSaida>, resumida: Boolean) {
    val report = RelatorioNotaDevolucao.processaRelatorio(notas, resumida, label == "Pendente")
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

  override fun excelRelatorio(notas: List<NotaSaida>): ByteArray {
    val report = RelatorioNotaFornecedor.processaExcel(notas)
    return report
  }

  override fun imprimirRelatorioResumido(fornecedores: List<Fornecedor>) {
    val report = RelatorioFornecedorResumido.processaRelatorio(fornecedores)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimirRelatorioPedidos(notas: List<NotaSaida>) {
    val report = RelatorioFornecedorPedido.processaRelatorio(notas)
    val chave = "DevFornecedorNotas"
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

  override fun updateNota() {
    dlgNota?.updateNota()
  }

  override val situacaoPedido: List<ESituacaoPedido>
    get() = emptyList()
}

