package br.com.astrosoft.devolucao.view.reports

import br.com.astrosoft.devolucao.model.beans.NotaSaida
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

class RelatorioNotaDevolucao(val notaSaida: NotaSaida) {
  val codigoCol = col.column("Cód Saci", ProdutosNotaSaida::codigo.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      //this.setPattern("000000")
      this.setFixedWidth(50)
    }
  val descricaoCol = col.column("Descrição", ProdutosNotaSaida::descricao.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      //this.setFixedWidth(60 * 4)
    }
  val gradeCol = col.column("Grade", ProdutosNotaSaida::grade.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(50)
    }
  val qtdeCol = col.column("Quant", ProdutosNotaSaida::qtde.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("0")
      this.setFixedWidth(40)
    }
  val itemCol = col.column("Item", ProdutosNotaSaida::item.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setPattern("000")
      this.setFixedWidth(25)
    }
  val valorUnitarioCol = col.column("V. Unit", ProdutosNotaSaida::valorUnitario.name, type.doubleType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(50)
    }
  val valorTotalCol = col.column("V. Total", ProdutosNotaSaida::valorTotal.name, type.doubleType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(60)
    }
  val barcodeCol = col.column("Cód Barra", ProdutosNotaSaida::barcode.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(80)
    }
  val unCol = col.column("Unid", ProdutosNotaSaida::un.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(30)
    }
  val niCol = col.column("NI", ProdutosNotaSaida::ni.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  val numeroNotaCol = col.column("Nota", ProdutosNotaSaida::numeroNota.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  val dataNotaCol = col.column("Data", ProdutosNotaSaida::dataNota.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  val qttdNotaCol = col.column("Quant", ProdutosNotaSaida::qttdNota.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  
  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      itemCol,
      barcodeCol,
      codigoCol,
      descricaoCol,
      gradeCol,
      unCol,
      qtdeCol,
      valorUnitarioCol,
      valorTotalCol
                 )
  }
  
  private fun titleBuider(): ComponentBuilder<*, *>? {
    return verticalList {
      horizontalFlowList {
        text("ENGECOPI", LEFT)
        text("PROCESSO INTERNO ${notaSaida.nota}", CENTER, 300)
        text("${
          LocalDate.now()
            .format()
        }-${
          LocalTime.now()
            .format()
        }", RIGHT)
      }
      horizontalFlowList {
        text("DEV FORNECEDOR: ${notaSaida.fornecedor}")
        text("COD FOR: ${notaSaida.vendno}", RIGHT, 200)
      }
    }
  }
  
  private fun sumaryBuild(): ComponentBuilder<*, *>? {
    return verticalList {
      breakLine()
      
      text("OBSERVAÇÕES:", LEFT, 100)
      text(notaSaida.obsNota, LEFT)
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
    var index: Int = 1
    val itens =
      notaSaida.listaProdutos()
        .sortedBy {it.codigo.toIntOrNull() ?: 0}
        .map {
          it.apply {
            item = index++
          }
        }
    return DynamicReports.report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(itens)
      .summary(sumaryBuild())
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
    fun processaRelatorio(listNota: List<NotaSaida>): ByteArray {
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