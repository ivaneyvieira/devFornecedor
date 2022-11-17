package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colBarcode
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCusto
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescricao
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colGrade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtde
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefFabrica
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colUnidade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colVlTotal
import br.com.astrosoft.devolucao.viewmodel.compra.TabPedidosViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.export.ExcelExporter
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgNotaProdutos(val viewModel: TabPedidosViewModel) {
  private lateinit var gridNota: Grid<PedidoCompraProduto>

  fun showDialogNota(pedido: PedidoCompra?) {
    pedido ?: return

    val produtos = pedido.produtos
    val form = SubWindowForm(pedido.labelTitle, toolBar = {
      button("Relatorio") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val produtos = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirPedidoCompra(PedidoCompra.group(produtos))
        }
      }
      this.lazyDownloadButtonXlsx("Planilha", "produtosCompra") {
        val produtos = gridNota.selectedItems.toList()
        val exporter = ExcelExporter(gridNota)
        exporter.exporterToByte("Produtos", produtos)
      }
    }) {
      gridNota = createGrid(produtos)
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
      colGrade()
      colRefFabrica()
      colDescNota()
      colRefNota()
      colUnidade()
      colQtde()
      colCusto()
      colVlTotal()
      colBarcode()
    }
  }
}
