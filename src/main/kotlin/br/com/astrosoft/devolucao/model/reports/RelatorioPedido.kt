package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NotaSaida
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
import net.sf.dynamicreports.report.builder.style.Styles
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration
import java.awt.Color
import java.io.ByteArrayOutputStream

class RelatorioPedido(val notas: List<NotaSaida>, val isExcel: Boolean) {
  private val colorFont
    get() = if (isExcel) Color.BLACK else Color.WHITE

  private val lojaCol: TextColumnBuilder<Int> = col.column("Loja", NotaSaida::loja.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setFixedWidth(35)
    this.setTextAdjust(TextAdjust.SCALE_FONT)
  }

  private val dataAgendaCol: TextColumnBuilder<String> =
    col.column("Data", NotaSaida::dataAgendaStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val dataNotaEditavelCol: TextColumnBuilder<String> =
    col.column("Data Nota", NotaSaida::dataNotaEditavelStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val pedidoCol: TextColumnBuilder<Int> =
    col.column("Pedido", NotaSaida::pedido.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val notaEditavelCol: TextColumnBuilder<String> =
    col.column("NF Baixa", NotaSaida::notaEditavel.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val dataPedidoStrCol: TextColumnBuilder<String> =
    col.column("Data", NotaSaida::dataPedidoStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val situacaoStrCol: TextColumnBuilder<String> =
    col.column("Situação", NotaSaida::situacaoStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val chaveDescontoCol: TextColumnBuilder<String> =
    col.column("Observação", NotaSaida::chaveDesconto.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setTextAdjust(TextAdjust.SCALE_FONT) //this.setFixedWidth(200)
    }

  private val valorNotaCol: TextColumnBuilder<Double> =
    col.column("Valor", NotaSaida::valorNota.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val emptyColumnValues = col.emptyColumn().apply {
    this.setFixedWidth(10)
    this.setTextAdjust(TextAdjust.SCALE_FONT)
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("Devoluções para Fornecedores", CENTER).apply {
          this.setStyle(fieldFontGrande)
          this.setStyle(stl.style().setForegroundColor(colorFont))
        }
      }
      val labelTitle = notas.firstOrNull()?.labelTitle2 ?: ""
      horizontalList {
        text("    $labelTitle", LEFT) {
          this.setStyle(stl.style().setForegroundColor(colorFont))
        }
      }
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  fun makeReport(): JasperReportBuilder {
    val pageOrientation = PageOrientation.LANDSCAPE
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(
        emptyColumnValues,
        lojaCol,
        dataPedidoStrCol,
        pedidoCol,
        dataNotaEditavelCol,
        notaEditavelCol,
        dataAgendaCol,
        situacaoStrCol,
        chaveDescontoCol,
        valorNotaCol,
      )
      .setDataSource(notas.sortedWith(compareBy({ it.loja }, { it.dataNota })))
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(
        fieldFontNormal
          .setForegroundColor(colorFont)
          .setFontSize(8)
          .setPadding(stl.padding().setRight(4).setLeft(4))
      )
      .setColumnTitleStyle(fieldFontNormalCol.setForegroundColor(colorFont).setFontSize(10))
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(colorFont).setPadding(Styles.padding().setTop(20)))
      .setGroupStyle(stl.style().setForegroundColor(colorFont))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)).setPadding(Styles.padding(20)))
  }

  companion object {
    fun processaRelatorio(notas: List<NotaSaida>): ByteArray {
      val report = RelatorioPedido(notas, isExcel = false).makeReport()
      val printList = listOf(report.toJasperPrint())
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }

    fun processaExcel(notas: List<NotaSaida>): ByteArray {
      val report = RelatorioPedido(notas, isExcel = true).makeReport()
      val printList = listOf(report.toJasperPrint())
      val exporter = JRXlsxExporter()
      val xlsReportConfiguration = SimpleXlsxReportConfiguration()
      xlsReportConfiguration.isOnePagePerSheet = false
      xlsReportConfiguration.isRemoveEmptySpaceBetweenRows = true
      xlsReportConfiguration.isRemoveEmptySpaceBetweenColumns = true
      xlsReportConfiguration.isDetectCellType = true
      xlsReportConfiguration.isFontSizeFixEnabled = true
      xlsReportConfiguration.isWhitePageBackground = false
      exporter.setConfiguration(xlsReportConfiguration)
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}
