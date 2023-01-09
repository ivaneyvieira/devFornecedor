package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colDataEntrega
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colDataPedido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colLoja
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colNumeroPedido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colObservacao
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlCancelada
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlPedida
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlPendente
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlRecebida
import br.com.astrosoft.devolucao.viewmodel.compra.ITabCompraViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.kaributools.fetchAll
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgNotaPedidoCompra(val viewModel: ITabCompraViewModel, val fornecedor: PedidoCompraFornecedor?) {
  private var form: SubWindowForm? = null
  private var dialog: DlgNotaProdutos? = null
  private lateinit var gridNota: Grid<PedidoCompra>

  fun showDialogNota() {
    fornecedor ?: return

    val pedidos = fornecedor.pedidos
    form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      button("Relatorio") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val pedidos = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirRelatorioFornecedor(pedidos)
        }
      }
      button("PDF") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirPedidoCompra(notas)
        }
      }
      this.lazyDownloadButtonXlsx("Planilha", "pedidosCompra") {
        val notas = gridNota.asMultiSelect().selectedItems.toList()
        viewModel.excelPedidoCompra(notas)
      }
    }) {
      gridNota = createGrid(pedidos)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form?.open()
  }

  private fun createGrid(listParcelas: List<PedidoCompra>): Grid<PedidoCompra> {
    return Grid(PedidoCompra::class.java, false).apply<Grid<PedidoCompra>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)
      addColumnButton(VaadinIcon.FILE_TABLE, "Produtos", "Prd") { pedido ->
        dialog = DlgNotaProdutos(viewModel, pedido)
        dialog?.showDialogNota()
      }
      colLoja()
      colNumeroPedido()
      colDataPedido()
      colDataEntrega()
      colObservacao()
      colVlPedida().let { col ->
        val lista = this.dataProvider.fetchAll()
        val total = lista.sumOf { it.vlPedido }.format()
        col.setFooter(Html("<b><font size=4>${total}</font></b>"))
      }
      colVlCancelada().let { col ->
        val lista = this.dataProvider.fetchAll()
        val total = lista.sumOf { it.vlCancelado }.format()
        col.setFooter(Html("<b><font size=4>${total}</font></b>"))
      }
      colVlRecebida().let { col ->
        val lista = this.dataProvider.fetchAll()
        val total = lista.sumOf { it.vlRecebido }.format()
        col.setFooter(Html("<b><font size=4>${total}</font></b>"))
      }
      colVlPendente().let { col ->
        val lista = this.dataProvider.fetchAll()
        val total = lista.sumOf { it.vlPendente }.format()
        col.setFooter(Html("<b><font size=4>${total}</font></b>"))
      }
    }
  }

  private fun updateComponent() {
    if (form?.isOpened == true) {
      viewModel.updateComponent()
      val pedidos = viewModel.listPedidosFornecedor().firstOrNull { pfor ->
        pfor.vendno == fornecedor?.vendno
      }?.pedidos.orEmpty()
      gridNota.setItems(pedidos)
    }
  }
}

val PedidoCompraFornecedor.labelTitle
  get() = "FORNECEDOR: $vendno $fornecedor CNPJ: $cnpj"