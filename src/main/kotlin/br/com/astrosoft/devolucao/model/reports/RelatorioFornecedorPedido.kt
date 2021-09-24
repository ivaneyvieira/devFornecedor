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
import net.sf.dynamicreports.report.builder.column.Columns
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.style.Styles
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.GroupHeaderLayout
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.awt.Color
import java.io.ByteArrayOutputStream

class RelatorioFornecedorPedido(val notas: List<NotaSaida>) {
  private val ESPACAMENTO = 125
  private val labelTitleCol: TextColumnBuilder<String> =
          col.column("", NotaSaida::labelTitlePedido.name, type.stringType()).apply {
            setHeight(50)
          }

  private val lojaCol: TextColumnBuilder<Int> = col.column("Loja", NotaSaida::loja.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(CENTER)
    this.setFixedWidth(60)
  }

  private val dataNotaCol: TextColumnBuilder<String> =
          col.column("Data", NotaSaida::dataNotaStr.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(80)
          }

  private val notaInvCol: TextColumnBuilder<String> =
          col.column("Pedido", NotaSaida::numeroNotaPedido.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(80)
          }

  private val valorCol: TextColumnBuilder<Double> =
          col.column("Valor", NotaSaida::valorNota.name, type.doubleType()).apply {
            this.setPattern("#,##0.00")
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(100)
          }

  private fun columnBuilder(): List<TextColumnBuilder<out Any>> {
    return listOf(emptyCol, lojaCol, dataNotaCol, notaInvCol, valorCol)
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("PEDIDO DEVOLUÇÃO FORNECEDOR", CENTER).apply {
          this.setStyle(fieldFontGrande.setForegroundColor(Color.WHITE))
        }
      }
    }
  }

  val emptyCol: TextColumnBuilder<String> = Columns.emptyColumn().apply {
    this.setFixedWidth(ESPACAMENTO)
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }


  fun makeReport(): JasperReportBuilder {
    val itemGroup =
            grp.group(labelTitleCol)
              .setTitleWidth(0)
              .setHeaderLayout(GroupHeaderLayout.VALUE)
              .showColumnHeaderAndFooter()

    val colunms = columnBuilder().toTypedArray()
    val pageOrientation = PORTRAIT
    return report().title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .setShowColumnTitle(false)
      .columns(* colunms)
      .columnGrid(* colunms)
      .groupBy(itemGroup)
      .addGroupFooter(itemGroup, cmp.text(""))
      .setDataSource(notas.sortedWith(compareBy({ it.custno }, { it.dataNota })))
      .setPageFormat(A4, pageOrientation)
      .summary(pageFooterBuilder())
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(fieldFontNormal)
      .setColumnTitleStyle(fieldFontNormalCol)
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(Styles.padding().setTop(20)))
      .setColumnStyle(stl.style().setForegroundColor(Color.WHITE))
      .setGroupStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(Styles.padding().setLeft(ESPACAMENTO +10)))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)))
  }

  companion object {
    fun processaRelatorio(notas: List<NotaSaida>): ByteArray {
      val printList = listOf(RelatorioFornecedorPedido(notas).makeReport().toJasperPrint())
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}

