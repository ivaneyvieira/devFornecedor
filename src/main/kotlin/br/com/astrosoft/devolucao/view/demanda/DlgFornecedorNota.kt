package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.FornecedorNota
import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaEmissao
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaEntrada
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaLoja
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaNF
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaNI
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaObs
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaValor
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class DlgFornecedorNota(val fornecedor: FornecedorProduto?) {
  private var form: SubWindowForm? = null
  private lateinit var gridNota: Grid<FornecedorNota>

  fun showDialogNota() {
    fornecedor ?: return

    val notas = fornecedor.findNotas()
    form = SubWindowForm(fornecedor.labelTitle, toolBar = {
    }) {
      gridNota = createGrid(notas)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form?.open()
  }

  private fun createGrid(listParcelas: List<FornecedorNota>): Grid<FornecedorNota> {
    return Grid(FornecedorNota::class.java, false).apply<Grid<FornecedorNota>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      fornecedorNotaLoja()
      fornecedorNotaNI()
      fornecedorNotaNF()
      fornecedorNotaEmissao()
      fornecedorNotaEntrada()
      fornecedorNotaValor()
      fornecedorNotaObs()
    }
  }

}
