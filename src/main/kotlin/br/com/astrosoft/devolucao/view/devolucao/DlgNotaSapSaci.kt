package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapData
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapNotaSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapNumero
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSapViewColumns.notaSapTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabConferenciaSapViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.list
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class DlgNotaSapSaci(val viewModel: TabConferenciaSapViewModel) {
  fun showDialogNota(fornecedor: FornecedorSap?) {
    fornecedor ?: return

    val listNotasSap = fornecedor.notas
    val listNotasSaci = fornecedor.notasSaci()
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridNota = createGridSap(listNotasSap, "Notas SAP")
      val gridPedido = createGridSaci(listNotasSaci, "Notas Saci")
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota, gridPedido)
      }
    }
    form.open()
  }

  private fun createGridSap(listParcelas: List<NotaDevolucaoSap>, label: String): GridLabel<NotaDevolucaoSap> {
    val gridDetail = Grid(NotaDevolucaoSap::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      notaSapLoja()
      notaSapNumero()
      notaSapNotaSaci()
      notaSapData()
      notaSapTotal().apply {
        val totalPedido = listParcelas.sumOf { it.saldo }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      //sort(listOf(GridSortOrder(getColumnBy(NotaDevolucaoSap::nfSaci), SortDirection.ASCENDING)))

      listParcelas.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }
    }
    return GridLabel(grid, label) {
      button("Relatório") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = grid.asMultiSelect().selectedItems.toList()
          viewModel.imprimirRelatorioSap(notas, label)
        }
      }
    }
  }

  private fun createGridSaci(listPedidos: List<NotaSaida>, label: String): GridLabel<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listPedidos)

      notaLoja()
      notaNota()
      notaFatura()
      notaDataNota()
      notaValor().apply {
        val totalPedido = listPedidos.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      //sort(listOf(GridSortOrder(getColumnBy(NotaSaida::nota), SortDirection.ASCENDING)))

      listPedidos.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }

    }
    return GridLabel(grid, label) {
      button("Relatório") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = grid.asMultiSelect().selectedItems.toList()
          viewModel.imprimirRelatorio(notas, label)
        }
      }
    }
  }
}

class GridLabel<T : Any>(val grid: Grid<T>, label: String, val toolbar: HorizontalLayout.() -> Unit = {}) :
        VerticalLayout() {
  fun selectRow(notaSaida: T?) {
    if (notaSaida != null) {
      grid.select(notaSaida)
      val list = grid.list()
      val index = list.indexOf(notaSaida)
      if (index >= 0) grid.scrollToIndex(index)
    }
    else grid.deselectAll()
  }

  fun onSelect(selectBlock: (T?) -> Unit) {
    grid.addSelectionListener {
      if (it.isFromClient) selectBlock(it.allSelectedItems.firstOrNull())
    }
  }

  init {
    this.horizontalLayout {
      setWidthFull()
      this.h3(label) {
        isExpand = true
      }
      this.toolbar()
    }

    this.addAndExpand(grid)
  }
}