package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.devolucao.model.beans.NotaEntradaNdd
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNotaSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNumeroPedido
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaTotal
import br.com.astrosoft.devolucao.viewmodel.entrada.TabAbstractEntradaNddViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.integerFieldEditor
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import br.com.astrosoft.framework.view.withEditor

class DlgNotaPainelNddSaci(val viewModel: TabAbstractEntradaNddViewModel<*>) {
  fun showDialogNota(fornecedor: FornecedorNdd?) {
    fornecedor ?: return

    val listNotasNdd = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridNota = createGridNdd(listNotasNdd)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGridNdd(listParcelas: List<NotaEntradaNdd>): Grid<NotaEntradaNdd> {
    return Grid(NotaEntradaNdd::class.java, false).apply<Grid<NotaEntradaNdd>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      this.withEditor(NotaEntradaNdd::class, openEditor = { binder ->
        (getColumnBy(NotaEntradaNdd::ordno).editorComponent as? Focusable<*>)?.focus()
      }, closeEditor = { binder ->
        viewModel.salvaNotaEntrada(binder.bean)
        this.dataProvider.refreshItem(binder.bean)
      })

      notaLoja()
      notaNotaSaci()
      notaNumeroPedido().integerFieldEditor()
      notaData()
      notaTotal().apply {
        val totalPedido = listParcelas.sumOf { it.baseCalculoIcms }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      //sort(listOf(GridSortOrder(getColumnBy(NotaDevolucaoSap::nfSaci), SortDirection.ASCENDING)))

      listParcelas.forEach { parcela ->
        setDetailsVisible(parcela, true)
      }
    }
  }
}