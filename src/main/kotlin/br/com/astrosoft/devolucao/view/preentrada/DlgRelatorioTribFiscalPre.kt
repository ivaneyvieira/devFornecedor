package br.com.astrosoft.devolucao.view.preentrada

import br.com.astrosoft.devolucao.model.beans.EValidade
import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDataEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaEstoque
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornCad
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsr
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpin
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpip
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvap
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaQuant
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRedIcms
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaValidade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVenc
import br.com.astrosoft.devolucao.view.entrada.columms.comboDiferencaStr
import br.com.astrosoft.devolucao.view.entrada.columms.marcaDiferenca
import br.com.astrosoft.devolucao.viewmodel.preentrada.TabTribFiscalPreViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.buttonPlanilha
import br.com.astrosoft.framework.view.selectedItemsSort
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.select
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioTribFiscalPre(val viewModel: TabTribFiscalPreViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NfPrecEntrada>
 // private val dataProviderGrid = ListDataProvider<NfPrecEntrada>(mutableListOf())

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {
      this.button("Relatório") {
        icon = PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorio(gridNota.selectedItemsSort())
        }
      }
      this.button("Relatório Resumo") {
        icon = PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorioResumo(gridNota.selectedItemsSort())
        }
      }
      buttonPlanilha("Planilha", FILE_EXCEL.create(), "planilhaNfPrecificacao") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort())
      }
      this.select<EValidade>("Validade") {
        setItems(EValidade.entries)
        value = EValidade.TODAS
        this.setItemLabelGenerator {
          it.descricao
        }

        this.addValueChangeListener {
          filtro.tipoValidade = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
      this.comboDiferencaStr("ICMS") {
        value = filtro.icms

        this.addValueChangeListener {
          filtro.icms = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
      this.comboDiferencaStr("IPI") {
        value = filtro.ipi

        this.addValueChangeListener {
          filtro.ipi = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
      this.comboDiferencaStr("CST") {
        value = filtro.cst

        this.addValueChangeListener {
          filtro.cst = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
      this.comboDiferencaStr("MVA") {
        value = filtro.mva

        this.addValueChangeListener {
          filtro.mva = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
    }) {
      gridNota = createGrid()
      val list = viewModel.findNotas(filtro)
      gridNota.setItems(list)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(): Grid<NfPrecEntrada> {
    return Grid(NfPrecEntrada::class.java, false).apply {
      setSizeFull()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)

      this.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_COLUMN_BORDERS)

      notaLoja()
      notaNi().marcaDiferenca { difGeral(true) }
      notaData()
      notaDataEmissao()
      notaNfe().marcaDiferenca { difGeral(true) }
      notaFornCad()
      notaFornNota()
      notaProd().marcaDiferenca { difGeral(true) }
      notaDescricao()
      notaGrade()
      notaQuant()
      notaVenc()
      notaValidade()
      notaEstoque()
      notaRedIcms().marcaDiferenca { icmsDif == "N" }
      notaIcmsr().marcaDiferenca { icmsDif == "N" }
      notaIcmsn().marcaDiferenca { icmsDif == "N" }
      notaIcmsp().marcaDiferenca { icmsDif == "N" }
      notaIpin().marcaDiferenca { ipiDif == "N" }
      notaIpip().marcaDiferenca { ipiDif == "N" }
      notaCstx().marcaDiferenca { cstDifxn == "N" }
      notaCst().marcaDiferenca { cstDifxn == "N" || cstDifnp == "N" }
      notaCstp().marcaDiferenca { cstDifnp == "N" }
      notaMvan().marcaDiferenca { mvaDif == "N" }
      notaMvap().marcaDiferenca { mvaDif == "N" }
    }
  }
}


