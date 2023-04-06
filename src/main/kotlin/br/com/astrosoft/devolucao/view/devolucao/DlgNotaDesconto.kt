package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorDesconto
import br.com.astrosoft.devolucao.model.beans.NotaEntradaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoDataEmissao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoDataEntrada
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoDataVencimento
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoNI
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoNumero
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoObs
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoValorDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoValorNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoValorPago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDescontoViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid

class DlgNotaDesconto(val viewModel: TabDescontoViewModel) {
  fun showDialogNota(fornecedor: FornecedorDesconto?) {
    fornecedor ?: return
    lateinit var gridNota: Grid<NotaEntradaDesconto>
    val listNotas = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {

    }) {
      gridNota = createGridNotas(listNotas)
      gridNota
    }
    form.open()
  }

  private fun createGridNotas(listNotas: List<NotaEntradaDesconto>): Grid<NotaEntradaDesconto> {
    val gridDetail = Grid(NotaEntradaDesconto::class.java, false)
    return gridDetail.apply {
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)

      notaDescontoLoja()
      notaDescontoNI()
      notaDescontoNumero()
      notaDescontoDataEmissao()
      notaDescontoDataEntrada()
      notaDescontoDataVencimento()

      notaDescontoValorNota().apply {
        val total = listNotas.sumOf { it.valor }.format()
        setFooter(Html("<b><font size=4 align='right'>${total}</font></b>"))
      }

      notaDescontoValorDesconto().apply {
        val total = listNotas.sumOf { it.desconto }.format()
        setFooter(Html("<b><font size=4>${total}</font></b>"))
      }

      notaDescontoValorPago().apply {
        val total = listNotas.sumOf { it.pagamento }.format()
        setFooter(Html("<b><font size=4>${total}</font></b>"))
      }

      notaDescontoObs()
    }
  }
}
