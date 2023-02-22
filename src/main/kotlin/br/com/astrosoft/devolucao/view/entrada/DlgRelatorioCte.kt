package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.EDifFrete
import br.com.astrosoft.devolucao.model.beans.EStatusFrete
import br.com.astrosoft.devolucao.model.beans.FiltroDialog
import br.com.astrosoft.devolucao.model.beans.NfEntradaFrete
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaAdValore
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaCte
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaCub
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaEmissaoCte
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaEntradaCte
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaFPeso
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaForNf
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaGris
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaICMS
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaNF
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaNI
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaOutros
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaPBruto
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaPesoCub
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaTaxa
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaTotalFrete
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaTotalNf
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaTotalPrd
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaTransp
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaTranspName
import br.com.astrosoft.devolucao.view.entrada.columms.NFECteColumns.notaValorFrete
import br.com.astrosoft.devolucao.viewmodel.entrada.TabCteViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.buttonPlanilha
import br.com.astrosoft.framework.view.selectedItemsSort
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.select
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.provider.ListDataProvider

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioCte(val viewModel: TabCteViewModel) {
  private lateinit var gridNota: Grid<NfEntradaFrete>
  private val dataProviderGrid = ListDataProvider<NfEntradaFrete>(mutableListOf())
  private var cmbStatus: Select<EStatusFrete>? = null
  private var cmbDifFrete: Select<EDifFrete>? = null

  fun updateGrid() {
    val list =
      viewModel.findNotas(FiltroDialog(status = cmbStatus?.value ?: EStatusFrete.TODOS,
                                       diferenca = cmbDifFrete?.value ?: EDifFrete.TODOS))
    gridNota.setItems(list)
  }

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {
      this.button("Relatório") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorio(gridNota.selectedItemsSort())
        }
      }
      this.button("Relatório Resumo") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorioResumo(gridNota.selectedItemsSort())
        }
      }
      buttonPlanilha("Planilha", FontAwesome.Solid.FILE_EXCEL.create(), "notaCte") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort())
      }
      cmbStatus = select("Situação") {
        setItems(EStatusFrete.values().toList())
        value = EStatusFrete.TODOS
        setItemLabelGenerator {
          it.descricao
        }
        addValueChangeListener {
          updateGrid()
        }
      }
      cmbDifFrete = select("R$ Frete") {
        setItems(EDifFrete.values().toList())
        value = EDifFrete.TODOS
        setItemLabelGenerator {
          it.descricao
        }
        addValueChangeListener {
          updateGrid()
        }
      }
    }) {
      gridNota = createGrid(dataProviderGrid)
      HorizontalLayout().apply {
        setSizeFull()
        updateGrid()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(dataProvider: ListDataProvider<NfEntradaFrete>): Grid<NfEntradaFrete> {
    return Grid(NfEntradaFrete::class.java, false).apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      this.dataProvider = dataProvider

      notaLoja()
      notaNI()
      notaNF()
      notaEmissao()
      notaEntrada()
      notaForNf()
      notaTotalPrd()
      notaTotalNf()
      notaTransp()
      notaTranspName()
      notaCte()
      notaEmissaoCte()
      notaEntradaCte()
      notaValorFrete().marcaDiferenca { freteDif }
      notaTotalFrete().marcaDiferenca { freteDif }
      notaPBruto()
      notaPesoCub()
      notaCub()
      notaFPeso()
      notaAdValore()
      notaGris()
      notaTaxa()
      notaOutros()
      notaICMS()
    }
  }
}

fun Grid.Column<NfEntradaFrete>.marcaDiferenca(predicado: NfEntradaFrete.() -> Boolean) {
  this.setClassNameGenerator {
    if (it.predicado()) "marcaDiferenca" else null
  }
}