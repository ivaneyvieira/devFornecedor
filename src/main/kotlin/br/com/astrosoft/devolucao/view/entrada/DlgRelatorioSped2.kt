package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.NotaEntradaHeadList
import br.com.astrosoft.devolucao.model.beans.group
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaHeadColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaHeadColumns.notaDataEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaHeadColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaHeadColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaHeadColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaHeadColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaBaseSubst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCFOP
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCst
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaGrade
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpin
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvap
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
import br.com.astrosoft.devolucao.viewmodel.entrada.TabSped2ViewModel
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
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.Column
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import kotlin.reflect.KProperty1

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioSped2(val viewModel: TabSped2ViewModel, val filtro: FiltroRelatorio) {
  private lateinit var gridNota: Grid<NotaEntradaHeadList>
  private val dataProviderGrid = ListDataProvider<NotaEntradaHeadList>(mutableListOf())

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {
      this.button("Relatório Resumo") {
        icon = PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorioResumo(gridNota.selectedItemsSort().flatMap { it.list })
        }
      }
      buttonPlanilha("Planilha", FILE_EXCEL.create(), "planilhaNfPrecificacao") {
        viewModel.geraPlanilha(gridNota.selectedItemsSort().flatMap { it.list })
      }
    }) {
      val list =
        viewModel.findNotas(filtro).groupBy { it.toHead() }.map {
          val nota = it.key
          val list = it.value
          NotaEntradaHeadList(
            nota.lj,
            nota.ni,
            nota.data,
            nota.dataEmissao,
            nota.nfe,
            nota.fornCad,
            nota.fornNota,
            list
          )
        }
      dataProviderGrid.items.clear()
      dataProviderGrid.items.addAll(list)
      gridNota = createGrid(dataProviderGrid)
      list.forEach {
        gridNota.setDetailsVisible(it, true)
      }

      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(dataProvider: ListDataProvider<NotaEntradaHeadList>): Grid<NotaEntradaHeadList> {
    return Grid(NotaEntradaHeadList::class.java, false).apply {
      setSizeFull()
      isMultiSort = false
      setSelectionMode(SelectionMode.MULTI)
      this.dataProvider = dataProvider
      this.addThemeVariants()

      notaLoja()
      notaNi()
      notaDataEmissao()
      notaData()
      notaNfe()
      notaFornNota()

      this.isDetailsVisibleOnClick = false
      this.setItemDetailsRenderer(createDetailsRenderer())
    }
  }

  private fun createDetailsRenderer() = ComponentRenderer(
    { GridProdutos() }, GridProdutos::setNota)
}

class GridProdutos : FormLayout(){
  val grid : Grid<NfPrecEntrada>
  fun setNota(nota: NotaEntradaHeadList) {
    val items = nota.list.group().sortedWith(compareBy({ it.ni }, { it.lj }, { it.prod }, { it.grade }))
    grid.setItems(items)
  }

  init {
    setResponsiveSteps(ResponsiveStep("0", 1))
    setHeightFull()

    grid = Grid<NfPrecEntrada>(NfPrecEntrada::class.java, false).apply {
      setSizeFull()
      isMultiSort = false
      setSelectionMode(SelectionMode.MULTI)

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
      notaValor().marcaTotal(NfPrecEntrada::valor)
      notaVlDesconto().marcaTotal(NfPrecEntrada::vlDesconto)
      notaVlLiquido().marcaTotal(NfPrecEntrada::vlLiquido)
      notaVlFrete().marcaTotal(NfPrecEntrada::vlFrete)
      notaVlDespesa().marcaTotal(NfPrecEntrada::vlDespesas)
      notaVlIcms().marcaTotal(NfPrecEntrada::vlIcms)
      notaVlIpi().marcaTotal(NfPrecEntrada::vlIpi)
      notaBaseSubst().marcaTotal(NfPrecEntrada::baseSubst)
      notaVlSubst().marcaTotal(NfPrecEntrada::vlIcmsSubst)
      notaVlTotal().marcaTotal(NfPrecEntrada::vlTotal)
      setClassNameGenerator {
        if (it?.lj == 999) "marcaTotal" else null
      }
    }
    add(grid)
  }

  private fun Column<NfPrecEntrada>.marcaTotal(prop: KProperty1<NfPrecEntrada, Double?>) {
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
