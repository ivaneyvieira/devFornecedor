package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.Fornecedor
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
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust.CUT_TEXT
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream

class RelatorioFornecedorResumido(val fornecedores: List<Fornecedor>) {
  private val codigoSapCol: TextColumnBuilder<Int> =
    col.column("Codigo SAP", Fornecedor::fornecedorSap.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(50)
      this.setPattern("0")
    }

  private val codigoSaciCol: TextColumnBuilder<Int> =
    col.column("Codigo Saci", Fornecedor::vendno.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
      this.setPattern("0")
    }

  private val nomeFornecedorCol: TextColumnBuilder<String> =
    col.column("Fornecedor", Fornecedor::fornecedor.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setTextAdjust(CUT_TEXT)
    }

  private val dataPrimeiraNotaCol: TextColumnBuilder<String> =
    col.column("Inicio", Fornecedor::primeiraDataStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(70)
    }

  private val dataUltimaNotaCol: TextColumnBuilder<String> =
    col.column("Fim", Fornecedor::primeiraDataStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(70)
    }

  private val saldoCol: TextColumnBuilder<Double> =
    col.column("Saldo Total", Fornecedor::valorTotal.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(80)
    }

  private fun columnBuilder(): List<TextColumnBuilder<out Any>> {
    return listOf(codigoSaciCol, codigoSapCol, nomeFornecedorCol, dataPrimeiraNotaCol, dataUltimaNotaCol, saldoCol)
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("FORNECEDOR", CENTER).apply {
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
      sbt.text("Total R$", dataUltimaNotaCol),
      sbt.sum(saldoCol),
                 )
  }

  fun makeReport(): JasperReportBuilder {
    val colunms = columnBuilder().toTypedArray()
    val pageOrientation = LANDSCAPE
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(fornecedores)
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(fieldFontNormal)
      .setColumnTitleStyle(fieldFontNormalCol)
  }

  companion object {
    fun processaRelatorio(fornecedores: List<Fornecedor>): ByteArray {
      val report = RelatorioFornecedorResumido(fornecedores).makeReport()
      val printList = listOf(report.toJasperPrint())
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}

