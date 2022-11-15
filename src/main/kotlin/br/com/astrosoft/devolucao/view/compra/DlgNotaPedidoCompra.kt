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
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlCancelada
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPedida
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPendente
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlRecebida
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNotaSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNumeroPedido
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaTemIPI
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaTotal
import br.com.astrosoft.devolucao.viewmodel.compra.TabPedidosViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.integerFieldEditor
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgNotaPedidoCompra(viewModel: TabPedidosViewModel) {
  fun showDialogNota(fornecedor: PedidoCompraFornecedor?) {
    fornecedor ?: return

    val pedidos = fornecedor.pedidos
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridNota = createGrid(pedidos)
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