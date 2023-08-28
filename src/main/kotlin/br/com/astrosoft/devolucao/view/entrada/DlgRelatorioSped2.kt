package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.EDiferencaNum
import br.com.astrosoft.devolucao.model.beans.EDiferencaStr
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
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaValor
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIcms
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIcmsx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIpi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIpix
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlSubstx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlTotal
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlTotalx
import br.com.astrosoft.devolucao.view.entrada.columms.comboDiferencaNum
import br.com.astrosoft.devolucao.view.entrada.columms.comboDiferencaStr
import br.com.astrosoft.devolucao.view.entrada.columms.marcaDiferenca
import br.com.astrosoft.devolucao.viewmodel.entrada.TabSped2ViewModel
import br.com.astrosoft.framework.util.format
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
class DlgRelatorioSped2(val viewModel: TabSped2ViewModel, val filtro: FiltroRelatorio) {
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
      this.comboDiferencaStr("CFOP") {
        value = filtro.cfop
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.cfop = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("CST") {
        value = filtro.cst
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.cst = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("MVA") {
        value = filtro.mva
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.mva = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("ICMS") {
        value = filtro.icms
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.icms = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("IPI") {
        value = filtro.ipi
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.ipi = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("Base ST") {
        value = filtro.baseST
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.baseST = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("valor ST") {
        value = filtro.valorST
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.valorST = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("Totla Nota") {
        value = filtro.totalNF
        this.setItems(EDiferencaStr.entries - EDiferencaStr.S)
        this.addValueChangeListener {
          filtro.totalNF = it.value
          updateGrid()
        }
      }
    }) {
      val list =
        viewModel.findNotas(filtro)
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

  private fun updateGrid() {
    val cfop = filtro.cfop
    filtro.cfop = EDiferencaStr.T
    val cst = filtro.cst
    filtro.cst = EDiferencaStr.T
    val mva = filtro.mva
    filtro.mva = EDiferencaStr.T
    val icms = filtro.icms
    filtro.icms = EDiferencaStr.T
    val ipi = filtro.ipi
    filtro.ipi = EDiferencaStr.T
    val baseST = filtro.baseST
    filtro.baseST = EDiferencaStr.T
    val valorST = filtro.valorST
    filtro.valorST = EDiferencaStr.T
    val totalNF = filtro.totalNF
    filtro.totalNF = EDiferencaStr.T
    val list = viewModel.findNotas(filtro).filter { nf ->
      (nf.cfopDifxp == cfop.str || cfop == EDiferencaStr.T) &&
          (nf.cstDifnp == cst.str || nf.cstDifxn == cst.str || cst == EDiferencaStr.T) &&
          (nf.mvaDifnp == mva.str || nf.mvaDifxn == mva.str || mva == EDiferencaStr.T) &&
          (nf.icmsDifxn == icms.str || icms == EDiferencaStr.T) &&
          (nf.ipiDifxn == ipi.str || ipi == EDiferencaStr.T) &&
          (nf.baseSubstxn == baseST.str || baseST == EDiferencaStr.T) &&
          (nf.vlIcmsSubstxn == valorST.str || valorST == EDiferencaStr.T) &&
          (nf.vlTotalxn == totalNF.str || totalNF == EDiferencaStr.T)
    }
    filtro.cfop = cfop
    filtro.cst = cst
    filtro.mva = mva
    filtro.icms = icms
    filtro.ipi = ipi
    filtro.baseST = baseST
    filtro.valorST = valorST
    filtro.totalNF = totalNF

    gridNota.setItems(list)
  }

  private fun createGrid(dataProvider: ListDataProvider<NfPrecEntrada>): Grid<NfPrecEntrada> {
    return Grid(NfPrecEntrada::class.java, false).apply {
      setSizeFull()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      this.dataProvider = dataProvider

      notaLoja()
      notaNi()
      notaData()
      notaNfe()
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
      notaValor().setHeader("Total N").marcaDiferenca { vlTotalxn == "N" }
    }
  }
}
