package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colDataEntrega
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colDataPedido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colLoja
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colNumeroPedido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colObservacao
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlCancelada
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlPedida
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlPendente
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraColumns.colVlRecebida
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colBarcode
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCusto
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescricao
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colGrade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtde
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefFabrica
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colUnidade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colVlTotal
import br.com.astrosoft.framework.view.SubWindowForm
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgNotaProdutos {
  fun showDialogNota(pedido: PedidoCompra?) {
    pedido ?: return

    val produtos = pedido.produtos
    val form = SubWindowForm(pedido.labelTitle, toolBar = {}) {
      val gridNota = createGrid(produtos)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(listParcelas: List<PedidoCompraProduto>): Grid<PedidoCompraProduto> {
    return Grid(PedidoCompraProduto::class.java, false).apply<Grid<PedidoCompraProduto>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)
      colCodigo()
      colDescricao()
      colRefFabrica()
      colGrade()
      colUnidade()
      colQtde()
      colCusto()
      colVlTotal()
      colBarcode()
    }
  }
}

val PedidoCompra.labelTitle
  get() = "LOJA: $loja PEDIDO: $numeroPedido"