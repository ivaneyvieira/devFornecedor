package br.com.astrosoft.devolucao.view.reports

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.view.reports.Templates.fieldBorder
import br.com.astrosoft.devolucao.view.reports.Templates.fieldFont
import br.com.astrosoft.devolucao.view.reports.Templates.fieldFontTitle
import br.com.astrosoft.framework.util.format
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.cmp
import net.sf.dynamicreports.report.builder.DynamicReports.col
import net.sf.dynamicreports.report.builder.DynamicReports.report
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
      this.setFixedWidth(40)
    }
  val refForCol = col.column("Ref do Fab", ProdutosNotaSaida::refFor.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(80)
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
      this.setFixedWidth(100)
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
  val invnoCol = col.column("NI", ProdutosNotaSaida::invno.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("0")
      this.setFixedWidth(50)
    }
  val quantInvCol = col.column("Quant NI", ProdutosNotaSaida::quantInv.name, type.integerType())
    .apply {
      this.setPattern("#,##0")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(40)
    }
  val notaInvCol = col.column("Nota", ProdutosNotaSaida::notaInv.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  val dateInvCol = col.column("Data", ProdutosNotaSaida::dateInvDate.name, type.dateType())
    .apply {
      this.setPattern("dd/MM/yyyy")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  val valorUnitInvCol = col.column("R$ Unit", ProdutosNotaSaida::valorUnitInv.name, type.doubleType())
    .apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  val valortotalInvCol = col.column("R$ Valor Total", ProdutosNotaSaida::valorTotalInv.name, type.doubleType())
    .apply {
      this.setPattern("#,##0.00")
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  
  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return if(notaSaida.tipo == "66" || notaSaida.tipo == "PED")
      listOf(
        itemCol,
        barcodeCol,
        refForCol,
        codigoCol,
        descricaoCol,
        gradeCol,
        unCol,
        invnoCol,
        quantInvCol,
        notaInvCol,
        dateInvCol,
        qtdeCol,
        valorUnitInvCol,
        valortotalInvCol
            )
    else listOf(
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
    return verticalBlock {
      horizontalList {
        val dup = if(notaSaida.tipo == "66" || notaSaida.tipo == "PED") "" else "DUP: ${notaSaida.fatura}"
        val dataAtual =
          LocalDate.now()
            .format()
        val horaAtual = LocalTime.now()
          .format()
        text("ENGECOPI   ${notaSaida.sigla}   PROCESSO ${notaSaida.nota}  -  ${notaSaida.dataNota.format()}  $dup",
             LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
      horizontalList {
        text("${notaSaida.custno} ${notaSaida.fornecedor}")
        text("COD FOR: ${notaSaida.vendno}", RIGHT, 200)
      }
    }
  }
  
  private fun sumaryBuild(): ComponentBuilder<*, *>? {
    return verticalBlock {
      if(notaSaida.tipo == "1") {
        breakLine()
        val widitInicial = 110
        this.horizontalList {
          this.verticalList {
            setStyle(fieldBorder)
            text("BASE DE CÁLCULO DO ICMS", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.baseIcms.format(), RIGHT) {this.setStyle(fieldFont)}
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR DO ICMS", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.valorIcms.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("BASE DE CÁLCULO DO ICMS SUBSTITUIÇÃO", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.baseIcmsSubst.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR ICMS SUBSTITUIÇÃO", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.icmsSubst.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR TOTAL DOS PRODUTOS", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.valorTotalProduto.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial)
          }
        }
        this.horizontalList {
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR DO FRETE", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.valorFrete.format(), RIGHT) {this.setStyle(fieldFont)}
          }
          
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR DO SEGURO", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.valorSeguro.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial * 2 * 3 / 8)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("DESCONTO", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.valorDesconto.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial * 2 * 2 / 8)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR ICMS SUBSTITUIÇÃO", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.icmsSubst.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial * 2 * 3 / 8)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("OUTRAS DESPESAS ACESSÓRIAS", LEFT) {this.setStyle(fieldFontTitle)}
            text(notaSaida.outrasDespesas.format(), RIGHT) {this.setStyle(fieldFont)}
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR TOTAL DA NOTA", LEFT) {
              this.setStyle(stl.style(fieldFontTitle)
                              .bold())
            }
            text(notaSaida.valorTotal.format(), RIGHT) {
              this.setStyle(stl.style(fieldFont)
                              .bold())
            }
            setWidth(widitInicial)
          }
        }
      }
      breakLine()
      
      text("OBSERVAÇÕES:", LEFT, 100)
      text(notaSaida.obsNota, LEFT)
      if(notaSaida.tipo == "66" || notaSaida.tipo == "PED")
        text(notaSaida.obsPedido)
    }
  }
  
  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }
  
  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return if(notaSaida.tipo == "66" || notaSaida.tipo == "PED") listOf(
      sbt.text("Total R$", valorUnitInvCol),
      sbt.sum(valortotalInvCol)
                                                                       )
    else
      listOf(
        sbt.text("Total R$", valorUnitarioCol),
        sbt.sum(valorTotalCol)
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
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(itens)
      .summary(sumaryBuild())
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style()
                          .setPadding(2)
                          .setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber()
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