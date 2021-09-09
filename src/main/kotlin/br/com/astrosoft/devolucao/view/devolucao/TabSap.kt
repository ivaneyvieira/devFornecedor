package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorSap
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorSapResumido
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorCodigoSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorCodigoSap
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorSaldoTotal
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabSap
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabSapViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

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
    this.lazyDownloadButtonXlsx("Planilha", "planilha") {
      viewModel.geraPlanilha(itensSelecionados())
    }
    this.lazyDownloadButtonXlsx("Planilha Resumo", "planilhaResumo") {
      viewModel.geraPlanilhaResumo(itensSelecionados())
    }
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

