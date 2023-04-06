package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaViewColumns.pedidoData
import br.com.astrosoft.devolucao.view.devolucao.columns.NotasEntradaViewColumns.pedidoNumero
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
import com.github.mvysny.karibudsl.v10.flexGrow
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
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
    val listNotasNaorecebidas = fornecedor.notasNaoRecebidasFornecedor()

    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {}) {
      HorizontalLayout().apply {
        setSizeFull()
        createGridPedidos(listPedidos, 3.0)
        createGridNotaEntradas(listNotasNaorecebidas, 6.0)
        createGridTitulos(listParcelas, 3.0)
      }
    }
    form.open()
  }

  fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  private fun HorizontalLayout.createGridTitulos(listParcelas: List<Parcela>, flex: Double): VerticalLayout {
    val gridDetail = Grid(Parcela::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      parcelaLoja()
      parcelaNi()
      parcelaNota()
      parcelaVencimento()
      parcelaValor().apply {
        val totalPedido = listParcelas.sumOf { it.valor }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
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
    return verticalLayout {
      this.flexGrow = flex
      this.h3("Títulos a Pagar")
      this.addAndExpand(grid)
    }
  }

  private fun HorizontalLayout.createGridPedidos(listPedidos: List<Pedido>, flex: Double): VerticalLayout {
    val gridDetail = Grid(Pedido::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listPedidos)

      pedidoLoja()
      pedidoNumero()
      pedidoData()
      pedidoTotal().apply {
        val totalPedido = listPedidos.sumOf { it.total }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
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
    return verticalLayout {
      this.flexGrow = flex
      this.h3("Pedidos de Compra Pendentes")
      this.addAndExpand(grid)
    }
  }

  private fun HorizontalLayout.createGridNotaEntradas(
    listEntradas: List<NotaEntradaNdd>,
    flex: Double
  ): VerticalLayout {
    val gridDetail = Grid(NotaEntradaNdd::class.java, false)
    val grid = gridDetail.apply {
      setSizeFull()
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listEntradas)

      pedidoNumero()
      pedidoData()
      notaValor().apply {
        val total = listEntradas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${total}</font></b>"))
      }

      val strTemplate =
        """<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'> 
          |<div>[[item.fatura]]</div>
          |</div>""".trimMargin()
      this.setItemDetailsRenderer(
        TemplateRenderer
          .of<NotaEntradaNdd?>(strTemplate)
          .withProperty("fatura", NotaEntradaNdd::linhaFatura)
      )
      listEntradas.forEach { parcela ->
        this.setDetailsVisible(parcela, true)
      }
    }
    return verticalLayout {
      this.flexGrow = flex
      this.h3("Notas a Receber")
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