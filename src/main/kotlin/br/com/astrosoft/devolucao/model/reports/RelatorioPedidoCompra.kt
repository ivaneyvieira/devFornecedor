package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.model.reports.*
import br.com.astrosoft.framework.model.reports.Templates.fieldBorder
import br.com.astrosoft.framework.model.reports.Templates.fieldFontGrande
import br.com.astrosoft.framework.util.format
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust.CUT_TEXT
import net.sf.dynamicreports.report.constant.TextAdjust.SCALE_FONT
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.JRXlsExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import net.sf.jasperreports.export.SimpleXlsReportConfiguration
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration
import java.io.ByteArrayOutputStream

class RelatorioPedidoCompra(val pedido: PedidoCompra) {
  private val itemCol: TextColumnBuilder<Int> =
    col.column("Item", ProdutosNotaSaida::item.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setPattern("000")
      this.setFixedWidth(25)
    }

  private val codigoCol: TextColumnBuilder<String> =
    col.column("Cód Saci", PedidoCompraProduto::codigo.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setTextAdjust(SCALE_FONT)
      this.setFixedWidth(35)
    }
  private val refForCol: TextColumnBuilder<String> =
    col.column("Ref do Fab", PedidoCompraProduto::refFab.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setTextAdjust(SCALE_FONT)
      this.setFixedWidth(60)
    }
  private val descricaoCol: TextColumnBuilder<String> =
    col.column("Descrição", PedidoCompraProduto::descricao.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setTextAdjust(CUT_TEXT)
    }
  private val gradeCol: TextColumnBuilder<String> =
    col.column("Grade", PedidoCompraProduto::grade.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setTextAdjust(SCALE_FONT)
      this.setFixedWidth(40)
    }

  private val qtdeCol: TextColumnBuilder<Int> =
    col.column("Quant", PedidoCompraProduto::qtPedida.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setTextAdjust(SCALE_FONT)
      this.setPattern("0")
      this.setFixedWidth(35)
    }

  private val valorUnitarioCol: TextColumnBuilder<Double> =
    col.column("V. Unit", PedidoCompraProduto::custoUnit.name, type.doubleType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setTextAdjust(SCALE_FONT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(40)
    }
  private val valorTotalCol: TextColumnBuilder<Double> =
    col.column("V. Total", PedidoCompraProduto::vlPedido.name, type.doubleType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setTextAdjust(SCALE_FONT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(40)
    }
  private val barcodeCol: TextColumnBuilder<String> =
    col.column("Cód Barra", PedidoCompraProduto::barcode.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(80)
    }

  private val unCol: TextColumnBuilder<String> =
    col.column("Unid", PedidoCompraProduto::unidade.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(30)
    }

  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      itemCol,
      barcodeCol,
      refForCol,
      codigoCol,
      descricaoCol,
      gradeCol,
      unCol,
      qtdeCol,
      valorUnitarioCol,
      valorTotalCol,
                 )
  }

  private fun titleBuiderPedido(): ComponentBuilder<*, *> {
    return verticalBlock {
      text("Pedido de Compra", CENTER).apply {
        this.setStyle(fieldFontGrande)
      }
      text("ENGECOPI ${pedido.sigla}", LEFT).apply {
        this.setStyle(fieldFontGrande)
      }
      horizontalList {
        val fornecedor = pedido.fornecedor
        val vendno = pedido.vendno
        val numeroPedido = pedido.numeroPedido
        val dataPedido = pedido.dataPedidoStr
        text("Fornecedor: $vendno - $fornecedor", LEFT)
        text("PED. $numeroPedido - $dataPedido", RIGHT, 150)
      }
    }
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return titleBuiderPedido()
  }

  private fun sumaryBuild(): ComponentBuilder<*, *> {
    return verticalBlock {
      sumaryPedido()

      breakLine()

      text("Dados Adicionais:", LEFT, 100)
      text(pedido.obsercacaoPedido)
    }
  }

  private fun VerticalListBuilder.sumaryPedido() {
    breakLine()
    this.horizontalList {
      this.verticalList {
        setStyle(fieldBorder)
        text("Valor Pedido", LEFT).fonteSumarioImposto()
        text(pedido.vlPedido.format(), RIGHT)
      }
      this.verticalList {
        setStyle(fieldBorder)
        text("Valor Cancelado", LEFT).fonteSumarioImposto()
        text(pedido.vlCancelado.format(), RIGHT)
      }
      this.verticalList {
        setStyle(fieldBorder)
        text("Valor Recebido", LEFT).fonteSumarioImposto()
        text(pedido.vlRecebido.format(), RIGHT)
      }
      this.verticalList {
        setStyle(fieldBorder)
        text("Valor Pendente", LEFT).fonteSumarioImposto()
        text(pedido.vlPendente.format(), RIGHT)
      }
    }
  }

  private fun TextFieldBuilder<String>.fonteSumarioImposto() {
    this.setTextAdjust(SCALE_FONT)
    this.setStyle(stl.style().setFontSize(8))
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return emptyList()
  }

  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    var index = 1
    val itens = pedido.produtos.sortedBy { it.codigo }.map {
      it.apply {
        item = index++
      }
    }
    val pageOrientation = PORTRAIT
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .setColumnStyle(stl.style().setFontSize(7))
      .columnGrid(* colunms)
      .setDataSource(itens)
      .summary(sumaryBuild())
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setFontSize(8).setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
  }

  companion object {
    fun processaRelatorio(pedidos: List<PedidoCompra>): ByteArray {
      val printList = pedidos.map { pedido ->
        val report = RelatorioPedidoCompra(pedido).makeReport()
        report?.toJasperPrint()
      }
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }

    fun processaExcel(pedidos: List<PedidoCompra>): ByteArray {
      val printList = pedidos.map { pedido ->
        val report = RelatorioPedidoCompra(pedido).makeReport()
        report?.toJasperPrint()
      }
      val exporter = JRXlsExporter()
      val xlsReportConfiguration = SimpleXlsxReportConfiguration()
      xlsReportConfiguration.isShowGridLines = true
      xlsReportConfiguration.isIgnorePageMargins = true
      xlsReportConfiguration.isDetectCellType = true
      xlsReportConfiguration.isRemoveEmptySpaceBetweenRows = false
      xlsReportConfiguration.isShrinkToFit = true
      exporter.setConfiguration(xlsReportConfiguration)
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}

