package br.com.astrosoft.devolucao.view.preentrada

import br.com.astrosoft.devolucao.model.beans.EDiferencaStr
import br.com.astrosoft.devolucao.model.beans.EDiferencaStr.S
import br.com.astrosoft.devolucao.model.beans.EDiferencaStr.T
import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBarcodec
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBarcodep
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBarcodex
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornCad
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaQuant
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaQuantx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRefPrdp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRefPrdx
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaUnidade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaUnidadex
import br.com.astrosoft.devolucao.view.entrada.columms.comboDiferencaStr
import br.com.astrosoft.devolucao.view.entrada.columms.marcaDiferenca
import br.com.astrosoft.devolucao.viewmodel.preentrada.TabPreRefFiscalViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.buttonPlanilha
import br.com.astrosoft.framework.view.selectedItemsSort
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.provider.ListDataProvider

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioPreRefFiscal(val viewModel: TabPreRefFiscalViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NfPrecEntrada>
  private val dataProviderGrid = ListDataProvider<NfPrecEntrada>(mutableListOf())
  private var cmbQuantDif: Select<EDiferencaStr>? = null

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
      buttonPlanilha("Planilha", FILE_EXCEL.create(), "planilhaPreRefFiscal") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort())
      }
      this.comboDiferencaStr("Referência") {
        value = filtro.refPrd

        this.addValueChangeListener {
          filtro.refPrd = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("Código de Barras") {
        value = filtro.barcode

        this.addValueChangeListener {
          filtro.barcode = it.value
          updateGrid()
        }
      }
      this.comboDiferencaStr("NCM") {
        value = filtro.ncm

        this.addValueChangeListener {
          filtro.ncm = it.value
          updateGrid()
        }
      }
      cmbQuantDif = this.comboDiferencaStr("Quantidade") {
        value = T

        this.addValueChangeListener {
          updateGrid()
        }
      }
      this.button("Ref XML") {
        this.icon = VaadinIcon.COGS.create()

        onLeftClick {
          viewModel.refXml()
        }
      }
      this.button("Barras XML") {
        this.icon = VaadinIcon.COGS.create()

        onLeftClick {
          viewModel.barrasXml()
        }
      }
      this.button("NCM XML") {
        this.icon = VaadinIcon.COGS.create()

        onLeftClick {
          viewModel.ncmXml()
        }
      }
    }) {
      gridNota = createGrid(dataProviderGrid)
      updateGrid()
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
      this.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_COLUMN_BORDERS)
      this.dataProvider = dataProvider

      notaLoja()
      notaNi().marcaDiferenca { difGeral() }
      notaData()
      notaNfe().marcaDiferenca { difGeral() }
      notaFornCad()
      notaFornNota()
      notaProd().marcaDiferenca { difGeral() }
      notaDescricao()
      notaGrade()
      notaUnidadex().apply {
        setHeader("Un X")
      }
      notaQuantx().apply {
        setHeader("Qtd X")
      }.marcaDiferenca { quantDifx != S }
      notaUnidade().apply {
        setHeader("Un S")
      }
      notaQuant().apply {
        setHeader("Qtd S")
      }.marcaDiferenca { quantDifx != S }
      notaRefPrdx().marcaDiferenca { refPrdDifx == "N" }
      notaRefPrdp().marcaDiferenca { refPrdDifx == "N" }

      notaBarcodex()
      notaBarcodep().marcaDiferenca { barcodeDifcp == "N" }
      notaBarcodec().marcaDiferenca { barcodeDifcx == "N" }

      notaNcmx().marcaDiferenca { ncmDifx == "N" }
      notaNcmp().marcaDiferenca { ncmDifx == "N" }
    }
  }

  private fun NfPrecEntrada.difGeral(): Boolean {
    return refPrdDifx == "N" || barcodeDifcx == "N" || barcodeDifcp == "N" || ncmDifx == "N"
  }

  fun selectedItemsSort(): List<NfPrecEntrada> {
    return gridNota.selectedItemsSort()
  }

  fun updateGrid() {
    val refPrd = filtro.refPrd
    filtro.refPrd = T
    val barcode = filtro.barcode
    filtro.barcode = T
    val ncm = filtro.ncm
    filtro.ncm = T
    val list = viewModel.findNotas(filtro).filter { nf ->
      (nf.refPrdDifx == refPrd.str || refPrd == T) &&
      (nf.barcodeDifcp == barcode.str || nf.barcodeDifcx == barcode.str || barcode == T) &&
      (nf.ncmDifx == ncm.str || ncm == T)
    }.filter { nf ->
      val quantDifx = cmbQuantDif?.value ?: return@filter false
      nf.quantDifx == quantDifx || quantDifx == T
    }
    filtro.refPrd = refPrd
    filtro.barcode = barcode
    filtro.ncm = ncm
    gridNota.setItems(list)
  }
}


