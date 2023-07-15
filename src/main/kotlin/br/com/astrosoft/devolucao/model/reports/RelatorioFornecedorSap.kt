package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
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

class RelatorioFornecedorSap(val notas: List<NotaDevolucaoSap>) {
  private val labelTitleCol: TextColumnBuilder<String> =
    col.column("", NotaDevolucaoSap::labelTitle.name, type.stringType()).apply {
      setHeight(50)
    }

  private val lojaCol: TextColumnBuilder<Int> =
    col.column("Loja", NotaDevolucaoSap::storeno.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(40)
    }

  private val dataNotaCol: TextColumnBuilder<String> =
    col.column("Data", NotaDevolucaoSap::dataLancamentoStr.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
    }

  private val notaInvCol: TextColumnBuilder<String> =
    col.column("Nota", NotaDevolucaoSap::numero.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(60)
    }

  private val valorCol: TextColumnBuilder<Double> =
    col.column("Valor", NotaDevolucaoSap::saldo.name, type.doubleType()).apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(100)
    }

  private fun columnBuilder(): List<TextColumnBuilder<out Any>> {
    return listOf(lojaCol, dataNotaCol, notaInvCol, valorCol)
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

  private fun subtotalBuilder(label: String): List<SubtotalBuilder<*, *>> {
    return listOf(
      sbt.text(label, notaInvCol),
      sbt.sum(valorCol),
    )
  }

  fun makeReport(): JasperReportBuilder {
    val itemGroup =
      grp.group(labelTitleCol).setTitleWidth(0).setHeaderLayout(GroupHeaderLayout.VALUE).showColumnHeaderAndFooter()

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
      .setDataSource(notas.sortedWith(compareBy({ it.codigoFor }, { it.dataLancamento })))
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtGroupFooter(itemGroup, * subtotalBuilder("Total R$").toTypedArray())
      .subtotalsAtSummary(* subtotalBuilder("Total Geral").toTypedArray())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(fieldFontNormal)
      .setColumnTitleStyle(fieldFontNormalCol)
  }

  companion object {
    fun processaRelatorioFornecedor(fornecedores: List<FornecedorSap>): ByteArray {
      val notas = fornecedores.flatMap { it.notas }
      return processaRelatorioNota(notas)
    }

    fun processaRelatorioNota(notas: List<NotaDevolucaoSap>): ByteArray {
      val printList = listOf(RelatorioFornecedorSap(notas).makeReport().toJasperPrint())
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}

