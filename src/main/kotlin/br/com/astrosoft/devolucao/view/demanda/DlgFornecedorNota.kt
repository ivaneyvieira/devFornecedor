package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.FiltroFornecedorNota
import br.com.astrosoft.devolucao.model.beans.FornecedorNota
import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaEmissao
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaEntrada
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaLoja
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaNF
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaNI
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaObs
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaObsParcela
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaSituacao
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaValor
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaVencimento
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.addColumnSeq
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class DlgFornecedorNota(val fornecedor: FornecedorProduto?) {
  private var form: SubWindowForm? = null
  private lateinit var gridNota: Grid<FornecedorNota>
  private lateinit var edtLoja: IntegerField
  private lateinit var edtQuery: TextField

  fun showDialogNota() {
    fornecedor ?: return

    form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      edtLoja = integerField ("Loja") {
        valueChangeMode = ValueChangeMode.LAZY
        valueChangeTimeout = 1000
        addValueChangeListener {
          updateGrid()
        }
      }
      edtQuery = textField("Pesquisa") {
        width = "300px"
        valueChangeMode = ValueChangeMode.LAZY
        valueChangeTimeout = 1000
        addValueChangeListener {
          updateGrid()
        }
      }
    }) {
      gridNota = createGrid()
      updateGrid()
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form?.open()
  }

  private fun updateGrid() {
    val notas = FornecedorNota.findByFornecedor(FiltroFornecedorNota(
      vendno =  fornecedor?.vendno ?: 0,
      loja = edtLoja.value ?: 0,
      query = edtQuery.value ?: "",
                                                                    ))
    gridNota.setItems(notas)
  }

  private fun createGrid(): Grid<FornecedorNota> {
    return Grid(FornecedorNota::class.java, false).apply<Grid<FornecedorNota>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)

      addColumnSeq("Item")
      fornecedorNotaLoja()
      addColumnButton(iconButton = VaadinIcon.FILE, tooltip = "Anexo", header = "Anexo") { nota ->
        val form = FormAnexoNota(nota, false) {
          updateGrid()
        }
        ConfirmDialog
          .create()
          .withCaption("Anexos")
          .withMessage(form)
          .withCloseButton(ButtonOption.caption("Fechar"))
          .open()
      }.apply {
        this.setClassNameGenerator { b ->
          if (b.quantAnexo > 0) "marcaOk" else null
        }
      }
      fornecedorNotaNI()
      fornecedorNotaNF()
      fornecedorNotaEmissao()
      fornecedorNotaEntrada()
      fornecedorNotaValor()
      fornecedorNotaObs()
      fornecedorNotaVencimento()
      fornecedorNotaSituacao()
      fornecedorNotaObsParcela()
    }
  }

}
