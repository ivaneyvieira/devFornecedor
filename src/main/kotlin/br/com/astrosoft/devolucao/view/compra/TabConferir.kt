package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorCompra
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorCompraResumido
import br.com.astrosoft.devolucao.model.reports.RelatorioPedidoCompra
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colDataPedido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colFornecedor
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlCancelada
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPedida
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPendente
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlRecebida
import br.com.astrosoft.devolucao.viewmodel.compra.ITabConferirViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.TabConferirViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.export.ExcelExporter
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.fetchAll
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabConferir(val viewModel: TabConferirViewModel) :
        TabPanelGrid<PedidoCompraFornecedor>(PedidoCompraFornecedor::class), ITabConferirViewModel {
  private lateinit var edtPedquisa: TextField
  private lateinit var edtLoja: IntegerField

  override fun HorizontalLayout.toolBarConfig() {
    edtLoja = integerField("Loja") {
      this.valueChangeMode = ValueChangeMode.TIMEOUT
      this.valueChangeTimeout = 1000
      this.addValueChangeListener {
        updateComponent()
      }
    }

    edtPedquisa = textField("Pesquisa") {
      this.valueChangeMode = ValueChangeMode.TIMEOUT
      this.valueChangeTimeout = 1000
      this.addValueChangeListener {
        updateComponent()
      }
    }
    button("Relatório") {
      icon = VaadinIcon.PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorioResumido(fornecedores)
      }
    }
    button("PDF") {
      icon = VaadinIcon.PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorioFornecedor(fornecedores.flatMap { it.pedidos })
      }
    }

    this.lazyDownloadButtonXlsx("Planilha", "fornecedorCompra") {
      val fornecedores = itensSelecionados()
      viewModel.excelRelatorioFornecedor(fornecedores.flatMap { it.pedidos })
    }
  }

  override fun Grid<PedidoCompraFornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Pedidos", "Pedidos") { fornecedor ->
      DlgNotaPedidoCompra(viewModel).showDialogNota(fornecedor)
    }

    colCodigo()
    colFornecedor()
    colDataPedido()
    colVlPedida()
    colVlCancelada()
    colVlRecebida()
    colVlPendente()
  }

  override fun filtro(): FiltroPedidoCompra {
    return FiltroPedidoCompra(
      loja = edtLoja.value ?: 0,
      pesquisa = edtPedquisa.value ?: "",
                             )
  }

  override fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>) {
    val report = RelatorioFornecedorCompra.processaRelatorio(pedido)
    val chave = "FornecedorCompra"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>) {
    val report = RelatorioFornecedorCompraResumido.processaRelatorio(fornecedores)
    val chave = "FornecedorCompra"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimeSelecionados(pedidos: List<PedidoCompra>) {
    val report = RelatorioPedidoCompra.processaRelatorio(pedidos)
    val chave = "PedidoCompra"
    SubWindowPDF(chave, report).open()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.compraConferir
  }

  override val label: String
    get() = "Conferir"

  override fun updateComponent() {
    viewModel.updateComponent()
  }
}