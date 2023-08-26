package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.EDiferencaStr.T
import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NotaXML
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlCofinsx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlIcmsx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlIpix
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlPisx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaBarcodex
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaCFOPX
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaCstx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaDataEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaDescricaox
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaMvax
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaRefPrdx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaUnidadex
import br.com.astrosoft.devolucao.viewmodel.entrada.TabXmlTribViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.selectedItemsSort
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.provider.ListDataProvider

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioXmlTrib(val viewModel: TabXmlTribViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NotaXML>
  private val dataProviderGrid = ListDataProvider<NotaXML>(mutableListOf())

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {

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

  private fun createGrid(dataProvider: ListDataProvider<NotaXML>): Grid<NotaXML> {
    return Grid(NotaXML::class.java, false).apply {
      setSizeFull()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      this.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_COLUMN_BORDERS)
      this.dataProvider = dataProvider

      notaLoja()
      notaNi()
      notaDataEmissao()
      notaData()
      notaNfe()
      notaRefPrdx().apply {
        setHeader("Referência")
      }
      notaBarcodex().apply {
        setHeader("EAN")
      }
      notaDescricaox()
      notaUnidadex()
      notaCFOPX().apply {
        setHeader("CFOP")
      }
      notaCstx().apply {
        setHeader("CST")
      }
      notaAlIcmsx().apply {
        setHeader("ICMS")
      }
      notaAlIpix().apply {
        setHeader("IPI")
      }
      notaMvax().apply {
        setHeader("MVA")
      }
      notaAlPisx().apply {
        setHeader("PIS")
      }
      notaAlCofinsx().apply {
        setHeader("COFINS")
      }
    }
  }

  fun selectedItemsSort(): List<NotaXML> {
    return gridNota.selectedItemsSort()
  }

  fun updateGrid() {
    filtro.refPrd = T
    filtro.barcode = T
    filtro.ncm = T
    val list = viewModel.findNotas(filtro)
    gridNota.setItems(list)
  }
}


