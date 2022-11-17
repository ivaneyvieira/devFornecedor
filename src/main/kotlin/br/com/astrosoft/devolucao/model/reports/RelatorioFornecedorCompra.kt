package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPedida
import br.com.astrosoft.framework.model.reports.Templates
import br.com.astrosoft.framework.model.reports.Templates.fieldFontGrande
import br.com.astrosoft.framework.model.reports.Templates.fieldFontNormal
import br.com.astrosoft.framework.model.reports.Templates.fieldFontNormalCol
import br.com.astrosoft.framework.model.reports.horizontalList
import br.com.astrosoft.framework.model.reports.text
import br.com.astrosoft.framework.model.reports.verticalBlock
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.GroupHeaderLayout
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream

class RelatorioFornecedorCompra(val pedido: List<PedidoCompra>) {
  private val labelTitleCol: TextColumnBuilder<String> =
    col.column("", PedidoCompra::labelGroup.name, type.stringType()).apply {
      setHeight(200)
    }

  private val labelGrupo: TextColumnBuilder<String> =
    col.column("", PedidoCompra::labelGroup.name, type.stringType()).apply {
      setHeight(50)
    }

  private val lojaCol: TextColumnBuilder<Int> = col.column("Lj", PedidoCompra::loja.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setFixedWidth(20)
  }

  private val dataNotaCol: TextColumnBuilder<String> =
    col.column("Data", PedidoCompra::dataPedidoStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
    }

  private val notaInvCol: TextColumnBuilder<Int> =
    col.column("Pedido", PedidoCompra::numeroPedido.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("0")
      this.setFixedWidth(60)
    }

  private val colVlPedida: TextColumnBuilder<Double> =
    col.column("Vl Pedida", PedidoCompra::vlPedido.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(90)
    }

  private val colVlCancelada: TextColumnBuilder<Double> =
    col.column("Vl Cancelada", PedidoCompra::vlCancelado.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(90)
    }

  private val colVlRecebida: TextColumnBuilder<Double> =
    col.column("Vl Recebida", PedidoCompra::vlRecebido.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(90)
    }

  private val colVlPendente: TextColumnBuilder<Double> =
    col.column("Vl Pendente", PedidoCompra::vlPendente.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(90)
    }

  private fun columnBuilder(): List<TextColumnBuilder<out Any>> {
    return listOf(lojaCol, dataNotaCol, notaInvCol, colVlPedida, colVlCancelada, colVlRecebida, colVlPendente)
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    val largura = 40 + 60 + 60 + 60 + 100
    return verticalBlock {
      horizontalList {
        text("FORNECEDOR", CENTER, largura).apply {
          this.setStyle(fieldFontGrande)
        }
      }
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return listOf(
      sbt.sum(colVlPedida),
      sbt.sum(colVlCancelada),
      sbt.sum(colVlRecebida),
      sbt.sum(colVlPendente),
                 )
  }

  fun makeReport(): JasperReportBuilder {
    val itemGroup =
      grp.group(labelGrupo).setTitleWidth(0).setHeaderLayout(GroupHeaderLayout.VALUE).showColumnHeaderAndFooter()

    val colunms = columnBuilder().toTypedArray()
    val pageOrientation = PORTRAIT
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .setShowColumnTitle(false)
      .columns(* colunms)
      .columnGrid(* colunms)
      .groupBy(itemGroup)
      .addGroupFooter(itemGroup, cmp.text(""))
      .setDataSource(pedido.sortedWith(compareBy({ it.vendno }, { it.loja }, { it.dataPedido })))
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtGroupFooter(itemGroup, * subtotalBuilder().toTypedArray())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(fieldFontNormal)
      .setColumnTitleStyle(fieldFontNormalCol)
  }

  companion object {
    fun processaRelatorio(pedido: List<PedidoCompra>): ByteArray {
      val printList = listOf(RelatorioFornecedorCompra(pedido).makeReport().toJasperPrint())
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}

