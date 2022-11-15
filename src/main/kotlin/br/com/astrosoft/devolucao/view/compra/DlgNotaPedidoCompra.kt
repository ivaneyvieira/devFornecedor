package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
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

      notaLoja()
      notaNotaSaci()
      notaNumeroPedido().integerFieldEditor()
      notaData()
      notaTemIPI()
      notaTotal().apply {
        val totalPedido = listParcelas.sumOf { it.baseCalculoIcms }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      listParcelas.forEach { parcela ->
        setDetailsVisible(parcela, true)
      }
    }
  }
}

val PedidoCompraFornecedor.labelTitle
  get() = "FORNECEDOR: $vendno $fornecedor CNPJ: $cnpj"