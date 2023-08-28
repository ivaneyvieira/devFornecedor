package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.EDiferencaNum
import br.com.astrosoft.devolucao.model.beans.EDiferencaNum.S
import br.com.astrosoft.devolucao.model.beans.EDiferencaStr.T
import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NotaXML
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlCofinsx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlIcmsx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlIpix
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaAlPisx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaBarcodex
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaCFOPX
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaCodigo
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaCstx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaDataEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaDescricaox
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaMvax
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaQuantidade
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaQuantidadeSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaRefPrdx
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaUnidadeSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaXMLColumns.notaUnidadex
import br.com.astrosoft.devolucao.view.entrada.columms.marcaDiferencaXml
import br.com.astrosoft.devolucao.viewmodel.entrada.TabXmlTribViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.buttonPlanilha
import br.com.astrosoft.framework.view.selectedItemsSort
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.select
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioXmlTrib(val viewModel: TabXmlTribViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NotaXML>
  private val dataProviderGrid = ListDataProvider<NotaXML>(mutableListOf())
  private var edtPesquisa: TextField? = null
  private var cmbDiferencaStr: Select<EDiferencaNum>? = null

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {
      edtPesquisa = textField("Pesquisa") {
        this.width = "40em"
        this.valueChangeMode = ValueChangeMode.LAZY
        this.valueChangeTimeout = 1000
        this.addValueChangeListener {
          updateGrid()
        }
      }
      cmbDiferencaStr = select("Quantidade") {
        this.setItems(EDiferencaNum.entries)
        this.value = EDiferencaNum.T
        this.setItemLabelGenerator {
          it.descricao
        }
        this.addValueChangeListener {
          updateGrid()
        }
      }
      this.buttonPlanilha("Planilha", FontAwesome.Solid.FILE_EXCEL.create(), "notaXml") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort())
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
      notaFornNota()
      notaRefPrdx().apply {
        setHeader("Referência")
      }
      notaBarcodex().apply {
        setHeader("EAN")
      }
      notaCodigo()
      notaDescricaox()
      notaUnidadex()
      notaQuantidade().marcaDiferencaXml { quantDiferenca() != S }
      notaUnidadeSaci()
      notaQuantidadeSaci().marcaDiferencaXml { quantDiferenca() != S }
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
    val query = edtPesquisa?.value ?: ""
    filtro.refPrd = T
    filtro.barcode = T
    filtro.ncm = T
    val list = viewModel.findNotas(filtro).filter { nota ->
      nota.toString().contains(query, true)
    }.filter { nota ->
      val dif = cmbDiferencaStr?.value ?: return@filter true
      if (dif == EDiferencaNum.T) return@filter true
      val difNota = nota.quantDiferenca()
      difNota == dif
    }
    gridNota.setItems(list)
  }
}


