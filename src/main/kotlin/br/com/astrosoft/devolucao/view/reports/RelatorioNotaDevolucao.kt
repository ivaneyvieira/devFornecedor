package br.com.astrosoft.devolucao.view.reports

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.util.format
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.builder.DynamicReports.col
import net.sf.dynamicreports.report.builder.DynamicReports.sbt
import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.DynamicReports.type
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime

class RelatorioNotaDevolucao(val notaDevolucao: NotaDevolucao) {
  val codigoCol = col.column("Código", ProdutosNotaSaida::codigo.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  val descricaoCol = col.column("Descrição", ProdutosNotaSaida::descricao.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
    }
  val gradeCol = col.column("Grade", ProdutosNotaSaida::grade.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(50)
    }
  val qtdeCol = col.column("Qtde", ProdutosNotaSaida::qtde.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("0")
      this.setFixedWidth(50)
    }
  
  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      qtdeCol, codigoCol,
      descricaoCol,
      gradeCol)
  }
  
  private fun titleBuider(): ComponentBuilder<*, *>? {
    return verticalList {
      horizontalFlowList {
        text("ENGECOPI", LEFT)
        text("PROCESSO INTERNO ${notaDevolucao.nota}", CENTER, 300)
        text("${
          LocalDate.now()
            .format()
        }-${
          LocalTime.now()
            .format()
        }", RIGHT)
      }
      horizontalFlowList {
        text("DEV FORNECEDOR: ${notaDevolucao.fornecedor}")
      }
    }
  }
  
  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return DynamicReports.cmp.verticalList()
  }
  
  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return listOf(
      sbt.sum(qtdeCol)
                 )
  }
  
  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    val itens = notaDevolucao.listaProdutos()
    return DynamicReports.report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(itens)
      .summary(pageFooterBuilder())
     // .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style()
                          .setPadding(2)
                          .setTopBorder(stl.pen1Point()))
      .pageFooter(DynamicReports.cmp.pageNumber()
                    .setHorizontalTextAlignment(RIGHT)
                    .setStyle(stl.style()
                                .setFontSize(8)))
  }
  
  companion object {
    fun processaRelatorio(listNota: List<NotaDevolucao>): ByteArray {
      val printList = listNota.map {nota ->
        val report = RelatorioNotaDevolucao(nota).makeReport()
        report?.toJasperPrint()
      }
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))
      
      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)
      
      exporter.exportReport()
      return out.toByteArray()
    }
  }
}