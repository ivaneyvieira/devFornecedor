package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.group
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBaseSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDataEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpin
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaQuant
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRotulo
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlIpi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlLiquido
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaVlTotal
import br.com.astrosoft.devolucao.viewmodel.entrada.TabSubstiFcViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.buttonPlanilha
import br.com.astrosoft.framework.view.selectedItemsSort
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.kaributools.fetchAll
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.provider.ListDataProvider
import kotlin.reflect.KProperty1

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioSubstiFc(val viewModel: TabSubstiFcViewModel, val filtro: FiltroRelatorio) {
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
      val list =
          viewModel.findNotas(filtro).group()
            .sortedWith(compareBy({ it.ni }, { it.lj }, { it.prod }, { it.grade }))
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
      notaNi()
      notaDataEmissao()
      notaData()
      notaNfe()
      notaFornNota().setHeader("For")
      notaProd().setHeader("Produto")
      notaDescricao().setHeader("Descrição")
      notaGrade().setHeader("Grade")
      notaRotulo()
      notaMvan()
      notaIpin().setHeader("IPI")
      notaQuant().setHeader("Qtd")
      notaVlLiquido().marcaTotal(NfPrecEntrada::vlLiquido)
      notaVlIpi().marcaTotal(NfPrecEntrada::vlIpi)
      notaBaseSubst().marcaTotal(NfPrecEntrada::baseSubst)
      notaVlSubst().marcaTotal(NfPrecEntrada::vlIcmsSubst)
      notaVlTotal().marcaTotal(NfPrecEntrada::vlTotal)
      setClassNameGenerator {
        if (it?.lj == 999) "marcaTotal" else null
      }
    }
  }

  private fun Grid.Column<NfPrecEntrada>.marcaTotal(prop: KProperty1<NfPrecEntrada, Double?>) {
    val lista = this.grid.dataProvider.fetchAll()
    val total = lista.sumOf {
      val bean = it as? NfPrecEntrada ?: return@sumOf 0.0
      if (bean.lj == 999) return@sumOf 0.0
      prop.get(bean) ?: 0.0
    }.format()
    val foot = setFooter(Html("<b><font size=4>${total}</font></b>"))
    foot.textAlign = ColumnTextAlign.END
  }
}


