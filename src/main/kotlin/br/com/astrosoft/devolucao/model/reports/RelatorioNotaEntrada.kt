package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntrada
import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntradaNota
import br.com.astrosoft.framework.model.reports.*
import br.com.astrosoft.framework.model.reports.Templates.fieldFontGrande
import br.com.astrosoft.framework.util.format
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust.CUT_TEXT
import net.sf.dynamicreports.report.constant.TextAdjust.SCALE_FONT
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream

class RelatorioNotaEntrada(val nota: MonitoramentoEntradaNota) {
  private val codigoCol: TextColumnBuilder<String> =
      col.column("Cód Saci", MonitoramentoEntrada::codigo.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(35)
      }
  private val dataInvCol: TextColumnBuilder<String> =
      col.column("Emissão", MonitoramentoEntrada::dataEntradaStr.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(40)
      }
  private val notaInvCol: TextColumnBuilder<String> =
      col.column("NF", MonitoramentoEntrada::nota.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(40)
      }
  private val refForCol: TextColumnBuilder<String> =
      col.column("Ref do Fab", MonitoramentoEntrada::refFor.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(60)
      }
  private val descricaoCol: TextColumnBuilder<String> =
      col.column("Descrição", MonitoramentoEntrada::descricao.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setTextAdjust(CUT_TEXT)
      }
  private val gradeCol: TextColumnBuilder<String> =
      col.column("Grade", MonitoramentoEntrada::grade.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(40)
      }

  private val invnoCol: TextColumnBuilder<Int> =
      col.column("NI", MonitoramentoEntrada::ni.name, type.integerType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setTextAdjust(SCALE_FONT)
        this.setPattern("0")
        this.setFixedWidth(30)
      }
  private val qtdeCol: TextColumnBuilder<Int> =
      col.column("Quant", MonitoramentoEntrada::quant.name, type.integerType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setTextAdjust(SCALE_FONT)
        this.setPattern("0")
        this.setFixedWidth(35)
      }
  private val itemCol: TextColumnBuilder<Int> =
      col.column("Item", MonitoramentoEntrada::item.name, type.integerType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setPattern("000")
        this.setFixedWidth(25)
      }
  private val valorUnitarioCol: TextColumnBuilder<Double> =
      col.column("V. Unit", MonitoramentoEntrada::valorUnit.name, type.doubleType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setTextAdjust(SCALE_FONT)
        this.setPattern("#,##0.00")
        this.setFixedWidth(40)
      }
  private val valorTotalCol: TextColumnBuilder<Double> =
      col.column("V. Total", MonitoramentoEntrada::valorTotal.name, type.doubleType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setTextAdjust(SCALE_FONT)
        this.setPattern("#,##0.00")
        this.setFixedWidth(40)
      }

  private val barcodeCol: TextColumnBuilder<String> =
      col.column("Cód Barra", MonitoramentoEntrada::barcode.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setFixedWidth(80)
      }

  private val unCol: TextColumnBuilder<String> =
      col.column("Unid", MonitoramentoEntrada::un.name, type.stringType()).apply {
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
      valorTotalCol
    )
  }

  private fun titleBuiderEntrada(): ComponentBuilder<*, *> {
    return verticalBlock {
      text("Espelho Nota Fiscal Entrada Fornecedor", CENTER).apply {
        this.setStyle(fieldFontGrande)
      }
      text("ENGECOPI ${nota.sigla}", LEFT).apply {
        this.setStyle(fieldFontGrande)
      }
      text("Natureza da operação: Compra", LEFT)
      horizontalList {
        val custno = nota.custno
        val fornecedor = nota.fornecedor
        val fornecedorSap = nota.fornecedorSap
        val vendno = nota.vendno
        val pedido = nota.pedido
        val dataPedido = nota.dataEntrada.format()
        text("Fornecedor: $custno - $fornecedor (FOR - $vendno  SAP - $fornecedorSap)", LEFT)
        text("PED. $pedido - $dataPedido", RIGHT, 150)
      }
    }
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return titleBuiderEntrada()
  }

  private fun sumaryBuild(): ComponentBuilder<*, *> {
    return verticalBlock {
      breakLine()

      text("Dados Adicionais:", LEFT, 100)
      text(nota.observacao, LEFT)
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    var index = 1
    val itens = nota.produtos.sortedBy { it.prdno }.map {
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
      .setDataSource(itens.toList())
      .summary(sumaryBuild())
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .setSubtotalStyle(stl.style().setFontSize(8).setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
  }

  companion object {
    fun processaRelatorio(listNota: List<MonitoramentoEntradaNota>): ByteArray {
      val printList = listNota.map { nota ->
        val report = RelatorioNotaEntrada(nota).makeReport()
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

