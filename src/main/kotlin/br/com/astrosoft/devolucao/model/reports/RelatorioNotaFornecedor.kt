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
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream

class RelatorioNotaFornecedor(val notas: List<NotaSaida>) {
  private val lojaCol: TextColumnBuilder<Int> = col.column("loja", NotaSaida::loja.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setFixedWidth(30)
  }

  private val dataNotaCol: TextColumnBuilder<String> =
          col.column("Data", NotaSaida::dataNotaStr.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(60)
          }

  private val notaInvCol: TextColumnBuilder<String> =
          col.column("Nota", NotaSaida::nota.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(60)
          }

  private val transfortadoraCol: TextColumnBuilder<String> =
          col.column("Transp", NotaSaida::transfortadora.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(LEFT)
          }

  private val conhecimentoFreteCol: TextColumnBuilder<String> =
          col.column("CTe", NotaSaida::conhecimentoFrete.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(40)
          }

  private val dataNfOrigemStrCol: TextColumnBuilder<String> =
          col.column("Data", NotaSaida::dataNfOrigemStr.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(60)
          }

  private val nfOrigemCol: TextColumnBuilder<String> =
          col.column("NF Origem", NotaSaida::nfOrigem.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(65)
          }

  private val dataCteStrCol: TextColumnBuilder<String> =
          col.column("Data", NotaSaida::dataCteStr.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(60)
          }

  private val obsOrigemCol: TextColumnBuilder<String> =
          col.column("Obs", NotaSaida::obsOrigem.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(LEFT)
          }

  private val valorCol: TextColumnBuilder<Double> =
          col.column("Valor", NotaSaida::valor.name, type.doubleType()).apply {
            this.setPattern("#,##0.00")
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(60)
          }

  private fun columnBuilder(): List<TextColumnBuilder<out Any>> {
    return listOf(
      lojaCol,
      dataNotaCol,
      notaInvCol,
      valorCol,
      nfOrigemCol,
      dataNfOrigemStrCol,
      transfortadoraCol,
      conhecimentoFreteCol,
      dataCteStrCol,
      obsOrigemCol,
                 )
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("Devoluções para Fornecedores", CENTER).apply {
          this.setStyle(fieldFontGrande)
        }
      }
      val labelTitle = notas.firstOrNull()?.labelTitle2 ?: ""
      horizontalList {
        text(labelTitle, LEFT)
      }
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return listOf(
      //      sbt.text("Total R$", faturaCol),
      sbt.sum(valorCol),
                 )
  }

  fun makeReport(): JasperReportBuilder {
    val colunms = columnBuilder().toTypedArray()
    val pageOrientation = PageOrientation.LANDSCAPE
    notas.forEach {
      it.findNotaOrigem()
    }
    return report().title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(notas)
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
    fun processaRelatorio(notas: List<NotaSaida>): ByteArray {
      val report = RelatorioNotaFornecedor(notas).makeReport()
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
