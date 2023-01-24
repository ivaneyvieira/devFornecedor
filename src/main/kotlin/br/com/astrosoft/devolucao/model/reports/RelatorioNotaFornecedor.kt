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
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.JRXlsExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import net.sf.jasperreports.export.SimpleXlsReportConfiguration
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration
import java.awt.Color
import java.io.ByteArrayOutputStream

class RelatorioNotaFornecedor(val notas: List<NotaSaida>, val isExcel: Boolean) {
  private val colorFont
    get() = if (isExcel) Color.BLACK else Color.WHITE
  private val colorForeground
    get() = if (isExcel) Color.WHITE else Color.BLACK

  private val lojaCol: TextColumnBuilder<Int> = col.column("Loja", NotaSaida::loja.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setFixedWidth(35)
    this.setTextAdjust(TextAdjust.SCALE_FONT)
  }

  private val dataNotaCol: TextColumnBuilder<String> =
    col.column("Data", NotaSaida::dataNotaStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val notaInvCol: TextColumnBuilder<String> =
    col.column("Nota", NotaSaida::nota.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val transfortadoraCol: TextColumnBuilder<String> =
    col.column("Transportadora", NotaSaida::transportadora.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val conhecimentoFreteCol: TextColumnBuilder<String> =
    col.column("CTe", NotaSaida::conhecimentoFrete.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val dataNfOrigemStrCol: TextColumnBuilder<String> =
    col.column("Data", NotaSaida::dataNfOrigemStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val nfOrigemCol: TextColumnBuilder<String> =
    col.column("Nota", NotaSaida::nfOrigem.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(60)
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }

  private val obsNotaCol: TextColumnBuilder<String> =
    col.column("Motivo", NotaSaida::remarks.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setTextAdjust(TextAdjust.SCALE_FONT) //this.setFixedWidth(200)
    }

  private val valorCol: TextColumnBuilder<Double> =
    col.column("Valor", NotaSaida::valor.name, type.doubleType()).apply {
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

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return listOf(
      sbt.text("Total R$", obsNotaCol).apply {
        this.setStyle(stl.style().setForegroundColor(colorFont).setTopBorder(stl.pen1Point()))
      },
      sbt.sum(valorCol).apply {
        this.setStyle(stl.style().setForegroundColor(colorFont).setTopBorder(stl.pen1Point()))
      },
                 )
  }

  fun makeReport(): JasperReportBuilder {
    val grupoOrigem =
      grid.titleGroup("Dados da Nota Fiscal de Origem",
                      dataNfOrigemStrCol,
                      nfOrigemCol,
                      conhecimentoFreteCol,
                      transfortadoraCol)
    val grupoDevolucao =
      grid.titleGroup("Dados da Nota Fiscal de Devolução", lojaCol, dataNotaCol, notaInvCol, obsNotaCol, valorCol)
    val pageOrientation = PageOrientation.LANDSCAPE
    notas.forEach {
      it.findNotaOrigem()
    }
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(emptyColumnValues,
               dataNfOrigemStrCol,
               nfOrigemCol,
               conhecimentoFreteCol,
               transfortadoraCol,
               lojaCol,
               dataNotaCol,
               notaInvCol,
               obsNotaCol,
               valorCol)
      .columnGrid(emptyColumnValues, grupoOrigem, grupoDevolucao, emptyColumnValues)
      .setDataSource(notas)
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(fieldFontNormal
                        .setForegroundColor(colorFont)
                        .setFontSize(8)
                        .setPadding(stl.padding().setRight(4).setLeft(4)))
      .setColumnTitleStyle(fieldFontNormalCol.setForegroundColor(colorForeground).setFontSize(10))
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(colorFont).setPadding(Styles.padding().setTop(20)))
      .setGroupStyle(stl.style().setForegroundColor(colorFont))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)).setPadding(Styles.padding(20)))
  }

  companion object {
    fun processaRelatorio(notas: List<NotaSaida>): ByteArray {
      val report = RelatorioNotaFornecedor(notas, isExcel = false).makeReport()
      val printList = listOf(report.toJasperPrint())
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }

    fun processaExcel(notas: List<NotaSaida>): ByteArray {
      val report = RelatorioNotaFornecedor(notas, isExcel = true).makeReport()
      val printList = listOf(report.toJasperPrint())
      val exporter = JRXlsxExporter()
      val xlsReportConfiguration = SimpleXlsxReportConfiguration()
      xlsReportConfiguration.isOnePagePerSheet = false
      xlsReportConfiguration.isRemoveEmptySpaceBetweenRows = true
      xlsReportConfiguration.isRemoveEmptySpaceBetweenColumns = true
      xlsReportConfiguration.isDetectCellType = true
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
