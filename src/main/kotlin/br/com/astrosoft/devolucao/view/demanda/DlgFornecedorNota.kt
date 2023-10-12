package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.FiltroFornecedorNota
import br.com.astrosoft.devolucao.model.beans.FornecedorNota
import br.com.astrosoft.devolucao.model.beans.FornecedorNotaExcel
import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorNota
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
import br.com.astrosoft.devolucao.viewmodel.demanda.TabFornecedorDemandaViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
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
import io.github.rushuat.ocell.document.DocumentOOXML
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class DlgFornecedorNota(val viewModel: TabFornecedorDemandaViewModel, val fornecedor: FornecedorProduto?) {
  private var form: SubWindowForm? = null
  private lateinit var gridNota: Grid<FornecedorNota>
  private lateinit var edtLoja: IntegerField
  private lateinit var edtQuery: TextField

  fun showDialogNota() {
    fornecedor ?: return

    form = SubWindowForm(fornecedor.labelTitle, toolBar = {
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
        val notas = gridNota.asMultiSelect().selectedItems.toList()
        excelFornecedorNota(notas)
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

  private fun excelFornecedorNota(notas: List<FornecedorNota>): ByteArray {
    return if (notas.isEmpty()) {
      viewModel.viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    } else {
      DocumentOOXML().use { document ->
        val notaExcel = notas.map { nota ->
          FornecedorNotaExcel(
            loja = nota.loja,
            ni = nota.ni,
            nf = nota.nf,
            emissao = nota.emissao.format(),
            entrada = nota.entrada.format(),
            vencimento = nota.vencimento.format(),
            valorNota = nota.valorNota,
            obs = nota.obs,
            situacao = nota.situacao,
            obsParcela = nota.obsParcela,
          )
        }
        document.addSheet(notaExcel)
        document.toBytes()
      }
    }
  }

  private fun updateGrid() {
    val notas =
        FornecedorNota.findByFornecedor(
          FiltroFornecedorNota(
            vendno = fornecedor?.vendno ?: 0,
            loja = edtLoja.value ?: 0,
            query = edtQuery.value ?: "",
          )
        )
    gridNota.setItems(notas.toList())
  }

  fun imprimirRelatorio(notas: List<FornecedorNota>) {
    if (notas.isEmpty()) {
      viewModel.viewModel.showError("Nenhuma item foi selecionado")
    } else {
      val report = RelatorioFornecedorNota.processaRelatorio(fornecedor?.labelTitle ?: "", notas)
      val chave = "DevFornecedorNota"
      SubWindowPDF(chave, report).open()
    }
  }

  private fun createGrid(): Grid<FornecedorNota> {
    return Grid(FornecedorNota::class.java, false).apply<Grid<FornecedorNota>> {
      setSizeFull()
      addThemeVariants()
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
      fornecedorNotaVencimento()
      fornecedorNotaValor()
      fornecedorNotaObs()
      fornecedorNotaSituacao()
      fornecedorNotaObsParcela()
    }
  }
}
