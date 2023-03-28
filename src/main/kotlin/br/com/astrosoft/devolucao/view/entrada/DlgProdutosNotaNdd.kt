package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoAliqICMS
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoAliqIPI
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoBaseIcms
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoCfop
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoCodBarra
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoCodigo
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoCst
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoNcm
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoQuantidade
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoUn
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorIpi
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorTotal
import br.com.astrosoft.devolucao.view.entrada.columms.ProdutosNotaNddColumns.produtoValorUnitario
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgProdutosNotaNdd(val produtos: List<ProdutoNotaEntradaNdd>, private val doPLanilha: () -> ByteArray) {
  private lateinit var gridNota: Grid<ProdutoNotaEntradaNdd>
  fun show() {
    val form = SubWindowForm("Produtos", toolBar = {
      lazyDownloadButtonXlsx("Planilha", "planilhaProduto") {
        doPLanilha()
      }
    }) {
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
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(produtos)

      produtoCodigo()
      produtoCodBarra()
      produtoDescricao()
      produtoNcm()
      produtoCst()
      produtoCfop()
      produtoUn()
      produtoQuantidade()
      produtoValorUnitario()
      produtoValorTotal()
      produtoBaseIcms()
      produtoValorIpi()
      produtoAliqIPI()
      produtoAliqICMS()
    }
  }
}