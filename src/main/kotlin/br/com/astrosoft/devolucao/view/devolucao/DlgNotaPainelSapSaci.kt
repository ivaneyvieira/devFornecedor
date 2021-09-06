package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapData
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapNotaSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapNumero
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabSapViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgNotaPainelSapSaci(val viewModel: TabSapViewModel) {
  fun showDialogNota(fornecedor: FornecedorSap?) {
    fornecedor ?: return

    val listNotasSap = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridNota = createGridSap(listNotasSap)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGridSap(listParcelas: List<NotaDevolucaoSap>): Grid<NotaDevolucaoSap> {
    val gridDetail = Grid(NotaDevolucaoSap::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      notaSapLoja()
      notaSapNumero()
      notaSapNotaSaci()
      notaSapData()
      notaSapTotal().apply {
        val totalPedido = listParcelas.sumOf { it.saldo }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      //sort(listOf(GridSortOrder(getColumnBy(NotaDevolucaoSap::nfSaci), SortDirection.ASCENDING)))

      listParcelas.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }
    }
    return grid
  }
}