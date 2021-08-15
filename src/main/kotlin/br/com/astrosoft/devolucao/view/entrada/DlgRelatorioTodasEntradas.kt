package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryCstn
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryFornCad
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryIcmsn
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryIcmsp
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryIcmsr
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryIpin
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryIpip
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryMvan
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryMvap
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryNfe
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryNi
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryProd
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryRedIcms
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornCad
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsr
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpin
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpip
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvap
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaRedIcms
import br.com.astrosoft.devolucao.viewmodel.entrada.TabTodasEntradasViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.provider.ListDataProvider

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioTodasEntradas(val viewModel: TabTodasEntradasViewModel, val list: List<NotaEntradaQuery>) {
  private lateinit var gridNota: Grid<NotaEntradaQuery>
  private val dataProviderGrid = ListDataProvider<NotaEntradaQuery>(mutableListOf())

  fun show() {
    val form = SubWindowForm("Relatório") {
      gridNota = createGrid(dataProviderGrid)
      gridNota.setItems(list)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(dataProvider: ListDataProvider<NotaEntradaQuery>): Grid<NotaEntradaQuery> {
    return Grid(NotaEntradaQuery::class.java, false).apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      this.dataProvider = dataProvider

      notaQueryLoja()
      notaQueryNi()
      notaQueryData()
      notaQueryNfe()
      notaQueryFornCad()
      notaQueryFornNota()
      notaQueryProd()
      notaQueryDescricao()
      notaQueryRedIcms()
      notaQueryIcmsr()
      notaQueryIcmsn()
      notaQueryIcmsp()
      notaQueryIpin()
      notaQueryIpip()
      notaQueryCstn()
      notaQueryMvan()
      notaQueryMvap()
    }
  }
}

