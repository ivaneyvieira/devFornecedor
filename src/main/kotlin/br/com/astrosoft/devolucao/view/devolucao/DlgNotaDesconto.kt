package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.FornecedorDesconto
import br.com.astrosoft.devolucao.model.beans.NotaEntradaDesconto
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoDataEmissao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoDataEnbtrada
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoNI
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoNumero
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoObs
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoValorDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoValorNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaDescontoViewColumns.notaDescontoValorPago
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDescontoViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection
import org.claspina.confirmdialog.ConfirmDialog

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
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)

      notaDescontoLoja()
      notaDescontoNI()
      notaDescontoNumero()
      notaDescontoDataEmissao()
      notaDescontoDataEnbtrada()

      notaDescontoValorNota().apply {
        val total = listNotas.sumOf { it.valor }.format()
        setFooter(Html("<b><font size=4>${total}</font></b>"))
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
