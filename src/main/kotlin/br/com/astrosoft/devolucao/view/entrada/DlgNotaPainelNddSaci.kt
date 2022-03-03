package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.devolucao.model.beans.NotaEntradaNdd
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNotaSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaNumeroPedido
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaTemIPI
import br.com.astrosoft.devolucao.view.entrada.columms.NotaNddViewColumns.notaTotal
import br.com.astrosoft.devolucao.viewmodel.entrada.TabAbstractEntradaNddViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.integerFieldEditor
import br.com.astrosoft.framework.view.withEditor
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

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

      this.withEditor(NotaEntradaNdd::class, openEditor = { _ ->
        (getColumnBy(NotaEntradaNdd::ordno).editorComponent as? Focusable<*>)?.focus()
      }, closeEditor = { binder ->
        viewModel.salvaNotaEntrada(binder.bean)
        this.dataProvider.refreshItem(binder.bean)
      })

      addColumnButton(iconButton = VaadinIcon.BULLETS, tooltip = "Produtos", header = "Prd") { nota ->
        DlgProdutosNotaNdd(nota.produtosNotaEntradaNDD).show()
      }

      addColumnButton(iconButton = VaadinIcon.PRINT, tooltip = "Nota fiscal", header = "NF") { nota ->
        viewModel.createDanfe(nota)
      }

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