package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoAliqDifICMS
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoAliqFrete
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoAliqIPI
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoAliqOutros
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoCodBarra
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoCodigo
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoQuantidadeAvaria
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoUn
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorDifICMS
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorFinalAvaria
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorFrete
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorIPIAvaria
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorOutrosAvaria
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorTotal
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorTotalAvaria
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorUnitario
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorUnitarioAvaria
import br.com.astrosoft.devolucao.viewmodel.entrada.TabAbstractEntradaNddViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.decimalFieldEditor
import br.com.astrosoft.framework.view.withEditor
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgAvariaNotaNdd(val produtos: List<ProdutoNotaEntradaNdd>, val viewModel: TabAbstractEntradaNddViewModel<*>) {
  private lateinit var gridNota: Grid<ProdutoNotaEntradaNdd>
  fun show() {
    val form = SubWindowForm("Produtos", toolBar = { }) {
      gridNota = createGrid()

      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(): Grid<ProdutoNotaEntradaNdd> {
    return Grid(ProdutoNotaEntradaNdd::class.java, false).apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(produtos)
      this.withEditor(ProdutoNotaEntradaNdd::class, openEditor = {
        val colunas = this.columns
        val componente = colunas.firstOrNull {
          it.editorComponent != null && (it.editorComponent is Focusable<*>)
        }
        val focusable = componente?.editorComponent as? Focusable<*>
        focusable?.focus()
      }, closeEditor = { binder -> //viewModel.salvaDesconto(binder.bean)
        this.dataProvider.refreshItem(binder.bean)
      })

      produtoCodigo()
      produtoCodBarra()
      produtoDescricao()
      produtoUn()
      produtoQuantidadeAvaria().decimalFieldEditor()
      produtoValorUnitario()
      produtoValorTotalAvaria()
      produtoAliqIPI()
      produtoValorIPIAvaria()
      produtoAliqOutros()
      produtoValorOutrosAvaria()
      produtoAliqFrete()
      produtoValorFrete()
      produtoAliqDifICMS()
      produtoValorDifICMS()
      produtoValorUnitarioAvaria()
      produtoValorFinalAvaria()
    }
  }
}