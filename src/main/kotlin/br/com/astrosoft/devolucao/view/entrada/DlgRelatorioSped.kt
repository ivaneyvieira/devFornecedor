package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBaseSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCFOP
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDataEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpin
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvap
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaQuant
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaValor
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlDesconto
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlDespesa
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlFrete
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIcms
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIpi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlLiquido
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlTotal
import br.com.astrosoft.devolucao.viewmodel.entrada.TabSpedViewModel
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
class DlgRelatorioSped(val viewModel: TabSpedViewModel, val filtro: FiltroRelatorio) {
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
      buttonPlanilha("Planilha", FILE_EXCEL.create(), "planilhaNfPrecificacao") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort())
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
      notaNi()
      notaDataEmissao()
      notaData()
      notaNfe()
      notaFornNota().setHeader("For")
      notaProd().setHeader("Produto")
      notaDescricao().setHeader("Descrição")
      notaGrade().setHeader("Grade")
      notaCFOP()
      notaCst()
      notaCstp()
      notaMvan()
      notaMvap()
      notaIcmsn().setHeader("ICMS")
      notaIpin().setHeader("IPI")
      notaQuant().setHeader("Qtd")
      notaValor()
      notaVlDesconto()
      notaVlLiquido()
      notaVlFrete()
      notaVlDespesa()
      notaVlIcms()
      notaVlIpi()
      notaBaseSubst()
      notaVlSubst()
      notaVlTotal()
    }
  }
}


