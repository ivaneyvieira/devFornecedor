package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBaseSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBaseSubstx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCFOP
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCFOPX
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvap
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvax
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIcms
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIcmsx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIpi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIpix
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlSubstx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlTotal
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlTotalx
import br.com.astrosoft.devolucao.view.entrada.columms.marcaDiferenca
import br.com.astrosoft.devolucao.viewmodel.entrada.TabSTEstadoViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.buttonPlanilha
import br.com.astrosoft.framework.view.selectedItemsSort
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.provider.ListDataProvider

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioSTEstado(val viewModel: TabSTEstadoViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NfPrecEntrada>
  private val dataProviderGrid = ListDataProvider<NfPrecEntrada>(mutableListOf())

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {
      this.button("Relatório Resumo") {
        icon = PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorioResumo(gridNota.selectedItemsSort())
        }
      }
      this.buttonPlanilha("Planilha", FILE_EXCEL.create(), "planilhaNfPrecificacao") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort())
      }
    }) {
      val list =
        viewModel.findNotas(filtro).filter {
          val cx = it.cstx ?: ""
          val cp = it.cstp ?: ""
          cx.endsWith("00") && cp == "06"
        }
      dataProviderGrid.items.clear()
      dataProviderGrid.items.addAll(list)
      gridNota = createGrid(dataProviderGrid)
      gridNota.setItems(list)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(dataProvider: ListDataProvider<NfPrecEntrada>): Grid<NfPrecEntrada> {
    return Grid(NfPrecEntrada::class.java, false).apply {
      setSizeFull()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      this.dataProvider = dataProvider

      notaLoja()
      notaProd().setHeader("Produto")
      notaDescricao().setHeader("Descrição")
      notaGrade().setHeader("Grade")
      notaCFOPX().marcaDiferenca { cfopDifxp == "N" }
      notaCFOP().marcaDiferenca { cfopDifxp == "N" }
      notaCstx().marcaDiferenca { cstDifxn == "N" }
      notaCst().marcaDiferenca { cstDifnp == "N" || cstDifxn == "N" }
      notaCstp().marcaDiferenca { cstDifnp == "N" }
      notaMvax().marcaDiferenca { mvaDifxn == "N" }
      notaMvan().marcaDiferenca { mvaDifnp == "N" || mvaDifxn == "N" }
      notaMvap().marcaDiferenca { mvaDifnp == "N" }
      notaVlIcmsx().setHeader("ICMS X").marcaDiferenca { icmsDifxn == "N" }
      notaVlIcms().setHeader("ICMS N").marcaDiferenca { icmsDifxn == "N" }
      notaVlIpix().setHeader("IPI X").marcaDiferenca { ipiDifxn == "N" }
      notaVlIpi().setHeader("IPI N").marcaDiferenca { ipiDifxn == "N" }
      notaBaseSubstx().setHeader("B ST X").marcaDiferenca { baseSubstxn == "N" }
      notaBaseSubst().setHeader("B ST N").marcaDiferenca { baseSubstxn == "N" }
      notaVlSubstx().setHeader("V ST X").marcaDiferenca { vlIcmsSubstxn == "N" }
      notaVlSubst().setHeader("V ST N").marcaDiferenca { vlIcmsSubstxn == "N" }
      notaVlTotalx().setHeader("Total X").marcaDiferenca { vlTotalxn == "N" }
      notaVlTotal().setHeader("Total N").marcaDiferenca { vlTotalxn == "N" }
      notaNi()
      notaData()
      notaNfe()
    }
  }
}
