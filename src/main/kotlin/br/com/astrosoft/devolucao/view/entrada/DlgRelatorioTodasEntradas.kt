package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryAliqIcms
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryAliqIpi
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryBaseIcms
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryCfop
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryCst
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryFornCad
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryFornNota
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryNcm
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryNfe
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryNi
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryProd
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryQuant
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryUn
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryValorIcms
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryValorIpi
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryValorTotal
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaQueryColumns.notaQueryValorUnit
import br.com.astrosoft.devolucao.viewmodel.entrada.TabTodasEntradasViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.selectedItemsSort
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.checkBox
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.provider.ListDataProvider

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioTodasEntradas(val viewModel: TabTodasEntradasViewModel, val list: List<NotaEntradaQuery>) {
  private lateinit var checkCst: Checkbox
  private lateinit var checkIcms: Checkbox
  private lateinit var checkIpi: Checkbox
  private lateinit var gridNota: Grid<NotaEntradaQuery>
  private val dataProviderGrid = ListDataProvider<NotaEntradaQuery>(mutableListOf())

  fun show() {
    val form = SubWindowForm("Relatório", toolBar = {
      this.button("Relatório") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val markCst = checkCst.value ?: false
          val markIpi = checkIpi.value ?: false
          val markIcms = checkIcms.value ?: false
          viewModel.imprimeRelatorio(gridNota.selectedItemsSort(), markCst, markIpi, markIcms)
        }
      }
      checkCst = checkBox("CST")
      checkIpi = checkBox("IPI")
      checkIcms = checkBox("ICMS")
    }) {
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
      notaQueryNcm()
      notaQueryCst()
      notaQueryCfop()
      notaQueryUn()
      notaQueryQuant()
      notaQueryValorUnit()
      notaQueryValorTotal()
      notaQueryBaseIcms()
      notaQueryValorIpi()
      notaQueryAliqIpi()
      notaQueryValorIcms()
      notaQueryAliqIcms()
    }
  }
}

