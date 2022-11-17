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
import br.com.astrosoft.devolucao.viewmodel.compra.TabPedidosViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.export.ExcelExporter
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgNotaPedidoCompra(val viewModel: TabPedidosViewModel) {
  private lateinit var gridNota: Grid<PedidoCompra>

  fun showDialogNota(fornecedor: PedidoCompraFornecedor?) {
    fornecedor ?: return

    val pedidos = fornecedor.pedidos
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {
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
        val notas = gridNota.selectedItems.toList()
        val exporter = ExcelExporter(gridNota)
        exporter.exporterToByte("Pedidos Compra", notas)
      }
    }) {
      gridNota = createGrid(pedidos)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(listParcelas: List<PedidoCompra>): Grid<PedidoCompra> {
    return Grid(PedidoCompra::class.java, false).apply<Grid<PedidoCompra>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)
      addColumnButton(VaadinIcon.FILE_TABLE, "Produtos", "Prd") { pedido ->
        DlgNotaProdutos(viewModel).showDialogNota(pedido)
      }
      colLoja()
      colNumeroPedido()
      colDataPedido()
      colDataEntrega()
      colObservacao()
      colVlPedida()
      colVlCancelada()
      colVlRecebida()
      colVlPendente()
    }
  }
}

val PedidoCompraFornecedor.labelTitle
  get() = "FORNECEDOR: $vendno $fornecedor CNPJ: $cnpj"