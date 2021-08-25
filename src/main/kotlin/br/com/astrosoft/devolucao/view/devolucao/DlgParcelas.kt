package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.Parcela
import br.com.astrosoft.devolucao.model.beans.Pedido
import br.com.astrosoft.devolucao.view.devolucao.columns.ParcelaViewColumns.parcelaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.ParcelaViewColumns.parcelaNi
import br.com.astrosoft.devolucao.view.devolucao.columns.ParcelaViewColumns.parcelaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.ParcelaViewColumns.parcelaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.ParcelaViewColumns.parcelaVencimento
import br.com.astrosoft.devolucao.view.devolucao.columns.PedidoViewColumns.pedidoData
import br.com.astrosoft.devolucao.view.devolucao.columns.PedidoViewColumns.pedidoLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.PedidoViewColumns.pedidoNumero
import br.com.astrosoft.devolucao.view.devolucao.columns.PedidoViewColumns.pedidoTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import com.github.mvysny.karibudsl.v10.h3
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.TemplateRenderer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DlgParcelas<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) {
  fun showDialogParcela(fornecedor: Fornecedor?, serie: Serie) {
    fornecedor ?: return

    val listParcelas = fornecedor.parcelasFornecedor()
    val listPedidos = fornecedor.pedidosFornecedor().filter {
      if (serie == Serie.FIN) it.observacao != "" else true
    }

    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      val gridParcela = createGridParcelas(listParcelas)
      val gridPedido = createGridPedidos(listPedidos)

      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridParcela, gridPedido)
      }
    }
    form.open()
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  private fun createGridParcelas(listParcelas: List<Parcela>): VerticalLayout {
    val gridDetail = Grid(Parcela::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      parcelaLoja()
      parcelaNi()
      parcelaNota()
      parcelaVencimento()
      parcelaValor().apply {
        val totalPedido = listParcelas.sumOf { it.valor }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      val strTemplate =
              """<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'> 
          |<div><b>OBS</b>: [[item.obs]]</div>
          |</div>""".trimMargin()
      this.setItemDetailsRenderer(TemplateRenderer.of<Parcela?>(strTemplate).withProperty("obs", Parcela::observacao))
      listParcelas.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }
    }
    return VerticalLayout().apply {
      this.h3("Títulos a Vencer")
      this.addAndExpand(grid)
    }
  }

  private fun createGridPedidos(listPedidos: List<Pedido>): VerticalLayout {
    val gridDetail = Grid(Pedido::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listPedidos)

      pedidoLoja()
      pedidoNumero()
      pedidoData()
      pedidoTotal().apply {
        val totalPedido = listPedidos.sumOf { it.total }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }

      val strTemplate =
              """<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'> 
          |<div><b>OBS</b>: [[item.obs]]</div>
          |</div>""".trimMargin()
      this.setItemDetailsRenderer(TemplateRenderer.of<Pedido?>(strTemplate).withProperty("obs", Pedido::observacao))
      listPedidos.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }
    }
    return VerticalLayout().apply {
      this.h3("Pedidos de Compra Pendentes")
      this.addAndExpand(grid)
    }
  }

  private fun configIconEdt(icon: Icon, nota: NotaSaida) {
    if (nota.rmk.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configMostraEmail(icon: Icon, nota: NotaSaida) {
    if (nota.listEmailNota().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configIconArq(icon: Icon, nota: NotaSaida) {
    if (nota.listFiles().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }
}