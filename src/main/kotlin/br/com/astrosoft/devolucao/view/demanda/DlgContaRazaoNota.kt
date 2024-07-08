package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaEmissao
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaEntrada
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaNF
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaNI
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaObs
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaObsParcela
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaSituacao
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaValor
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorNotaColumns.fornecedorNotaVencimento
import br.com.astrosoft.devolucao.viewmodel.demanda.TabContaRazaoDemandaViewModel
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class DlgContaRazaoNota(val viewModel: TabContaRazaoDemandaViewModel, val contaRazao: ContaRazao?) {
  private var form: SubWindowForm? = null
  private lateinit var gridNota: Grid<ContaRazaoNota>
  private lateinit var edtLoja: IntegerField
  private lateinit var edtQuery: TextField

  fun showDialogNota() {
    contaRazao ?: return

    form = SubWindowForm(contaRazao.labelTitle, toolBar = {
      edtLoja = integerField("Loja") {
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
      this.lazyDownloadButtonXlsx("Planilha", "pedidosCompra") {
        ByteArray(0)
      }
      button("Relatório") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          imprimirRelatorio(notas)
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
    val notas = contaRazao?.notas.orEmpty()
    gridNota.setItems(notas.toList())
  }

  fun imprimirRelatorio(notas: List<ContaRazaoNota>) {
    if (notas.isEmpty()) {
      viewModel.viewModel.showError("Nenhuma item foi selecionado")
    } else {
      //val report = RelatorioFornecedorNota.processaRelatorio(contaRazao?.labelTitle ?: "", notas)
      //val chave = "DevFornecedorNota"
      //SubWindowPDF(chave, report).open()
    }
  }

  private fun createGrid(): Grid<ContaRazaoNota> {
    return Grid(ContaRazaoNota::class.java, false).apply<Grid<ContaRazaoNota>> {
      setSizeFull()
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)

      addColumnSeq("Item")
      columnGrid(ContaRazaoNota::loja, "Loja")
      /*
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
      }*/
      columnGrid(ContaRazaoNota::ni, "NI")
      columnGrid(ContaRazaoNota::nf, "NF")
      columnGrid(ContaRazaoNota::emissao, "Emissão")
      columnGrid(ContaRazaoNota::entrada, "Entrada")
      columnGrid(ContaRazaoNota::vencimento, "Vencimento")
      columnGrid(ContaRazaoNota::valorNota, "Valor Nota")
      columnGrid(ContaRazaoNota::vendno, "Fornecedor")
      columnGrid(ContaRazaoNota::fornecedor, "Fornecedor Nome")
      columnGrid(ContaRazaoNota::obs, "Obs")
      columnGrid(ContaRazaoNota::situacao, "Situação")
      columnGrid(ContaRazaoNota::obsParcela, "Obs Parcela")
    }
  }
}
