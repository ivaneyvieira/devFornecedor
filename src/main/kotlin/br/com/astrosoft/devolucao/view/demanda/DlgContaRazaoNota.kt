package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.ContaRazao
import br.com.astrosoft.devolucao.model.beans.ContaRazaoNota
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaContaRazaoNotas
import br.com.astrosoft.devolucao.model.reports.RelatorioContaRazaoNota
import br.com.astrosoft.devolucao.viewmodel.demanda.TabContaRazaoDemandaViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.getColumnBy
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
        val list = gridNota.selectedItems.toList()
        PlanilhaContaRazaoNotas().grava(list)
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
    val notas = contaRazao?.notas.orEmpty().filter {
      val query = edtQuery.value ?: ""
      val loja = edtLoja.value ?: 0
      (query == "" ||
       (it.ni.toString().contains(query, true)) ||
       (it.nf?.contains(query, true) == true) ||
       (it.fornecedor?.contains(query, true) == true) ||
       it.vendno.toString().contains(query, true) ||
       (it.situacao?.contains(query, true) == true) ||
       (it.obsParcela?.contains(query, true) == true) ||
       (it.obs?.contains(query, true) == true))
        && (loja == 0 || it.loja == loja)
    }
    gridNota.setItems(notas.toList())
    gridNota.getColumnBy(ContaRazaoNota::valorNota).setFooter(notas.sumOf { it.valorNota ?: 0.0 }.format())
    gridNota.getColumnBy(ContaRazaoNota::vencimento).setFooter("Total")
  }

  fun imprimirRelatorio(notas: List<ContaRazaoNota>) {
    if (notas.isEmpty()) {
      viewModel.viewModel.showError("Nenhuma item foi selecionado")
    } else {
      val report = RelatorioContaRazaoNota.processaRelatorio(contaRazao?.labelTitle ?: "", notas)
      val chave = "DevFornecedorNota"
      SubWindowPDF(chave, report).open()
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
