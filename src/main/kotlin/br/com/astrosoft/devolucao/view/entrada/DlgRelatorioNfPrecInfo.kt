package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBarcoden
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBarcodep
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornCad
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRefPrdn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRefPrdp
import br.com.astrosoft.devolucao.view.entrada.columms.comboDiferencaStr
import br.com.astrosoft.devolucao.view.entrada.columms.marcaDiferenca
import br.com.astrosoft.devolucao.viewmodel.entrada.TabNfPrecInfoViewModel
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
class DlgRelatorioNfPrecInfo(val viewModel: TabNfPrecInfoViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NfPrecEntrada>
  private val dataProviderGrid = ListDataProvider<NfPrecEntrada>(mutableListOf())

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
      this.comboDiferencaStr("Ref") {
        value = filtro.refPrd

        this.addValueChangeListener {
          filtro.refPrd = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
      this.comboDiferencaStr("Código de Barras") {
        value = filtro.barcode

        this.addValueChangeListener {
          filtro.barcode = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
      this.comboDiferencaStr("NCM") {
        value = filtro.ncm

        this.addValueChangeListener {
          filtro.ncm = it.value
          val list = viewModel.findNotas(filtro)
          gridNota.setItems(list)
        }
      }
    }) {
      gridNota = createGrid(dataProviderGrid)
      val list = viewModel.findNotas(filtro)
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
      notaNi().marcaDiferenca { difGeral(false) }
      notaData()
      notaNfe().marcaDiferenca { difGeral(false) }
      notaFornCad()
      notaFornNota()
      notaProd().marcaDiferenca { difGeral(false) }
      notaDescricao()
      notaGrade()
      notaRefPrdn().marcaDiferenca { refPrdDif == "N" }
      notaRefPrdp().marcaDiferenca { refPrdDif == "N" }
      notaBarcoden().marcaDiferenca { barcodeDif == "N" }
      notaBarcodep().marcaDiferenca { barcodeDif == "N" }
      notaNcmn().marcaDiferenca { ncmDif == "N" }
      notaNcmp().marcaDiferenca { ncmDif == "N" }
    }
  }
}


