package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.model.reports.*
import br.com.astrosoft.framework.model.reports.Templates.fieldBorder
import br.com.astrosoft.framework.model.reports.Templates.fieldFont
import br.com.astrosoft.framework.model.reports.Templates.fieldFontGrande
import br.com.astrosoft.framework.model.reports.Templates.fieldFontTitle
import br.com.astrosoft.framework.util.format
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime

class RelatorioNotaDevolucao(val notaSaida: NotaSaida, private val resumida: Boolean) {
  private val codigoCol: TextColumnBuilder<String> =
          col.column("Cód Saci", ProdutosNotaSaida::codigo.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setFixedWidth(40)
          }
  private val refForCol: TextColumnBuilder<String> =
          col.column("Ref do Fab", ProdutosNotaSaida::refFor.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(80)
          }
  private val descricaoCol: TextColumnBuilder<String> =
          col.column("Descrição", ProdutosNotaSaida::descricao.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(LEFT) //this.setFixedWidth(60 * 4)
          }
  private val gradeCol: TextColumnBuilder<String> =
          col.column("Grade", ProdutosNotaSaida::grade.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(50)
          }
  private val stCol: TextColumnBuilder<String> = col.column("ST", ProdutosNotaSaida::st.name, type.stringType()).apply {
    this.setHorizontalTextAlignment(CENTER)
    this.setFixedWidth(25)
  }
  private val qtdeCol: TextColumnBuilder<Int> =
          col.column("Quant", ProdutosNotaSaida::qtde.name, type.integerType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setPattern("0")
            this.setFixedWidth(40)
          }
  private val itemCol: TextColumnBuilder<Int> =
          col.column("Item", ProdutosNotaSaida::item.name, type.integerType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setPattern("000")
            this.setFixedWidth(25)
          }
  private val valorUnitarioCol: TextColumnBuilder<Double> =
          col.column("V. Unit", ProdutosNotaSaida::valorUnitario.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(50)
          }
  private val valorTotalCol: TextColumnBuilder<Double> =
          col.column("V. Total", ProdutosNotaSaida::valorTotal.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(60)
          }
  private val valorTotalIpiCol: TextColumnBuilder<Double> =
          col.column("R$ Total Geral", ProdutosNotaSaida::valorTotalIpi.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(60)
          }
  private val ipiCol: TextColumnBuilder<Double> =
          col.column("Valor Ipi", ProdutosNotaSaida::ipi.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(50)
          }
  private val vstCol: TextColumnBuilder<Double> =
          col.column("Valor ST", ProdutosNotaSaida::vst.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(50)
          }
  private val barcodeCol: TextColumnBuilder<String> =
          col.column("Cód Barra", ProdutosNotaSaida::barcode.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(80)
          }

  private val cstCol: TextColumnBuilder<String> =
          col.column("CST", ProdutosNotaSaida::cst.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(25)
          }

  private val cfopCol: TextColumnBuilder<String> =
          col.column("CFOP", ProdutosNotaSaida::cfop.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(25)
          }


  private val unCol: TextColumnBuilder<String> =
          col.column("Unid", ProdutosNotaSaida::un.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(30)
          }

  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return when (notaSaida.tipo) {
      "66", "PED", "AJT", "FIN" -> if (resumida) listOf(itemCol,
                                                        barcodeCol,
                                                        refForCol,
                                                        codigoCol,
                                                        descricaoCol,
                                                        gradeCol,
                                                        unCol,
                                                        qtdeCol)
      else listOf(itemCol,
                  barcodeCol,
                  refForCol,
                  codigoCol,
                  descricaoCol,
                  gradeCol,
                  unCol,
                  stCol,
                  qtdeCol,
                  valorUnitarioCol,
                  valorTotalCol,
                  ipiCol,
                  vstCol,
                  valorTotalIpiCol)
      else                      -> listOf(itemCol,
                                          barcodeCol,
                                          codigoCol,
                                          descricaoCol,
                                          gradeCol,
                                          cstCol,
                                          cfopCol,
                                          unCol,
                                          qtdeCol,
                                          valorUnitarioCol,
                                          valorTotalCol)
    }
  }

  private fun titleBuiderPedido(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("ENGECOPI ${notaSaida.sigla}", CENTER).apply {
          this.setStyle(fieldFontGrande)
        }
      }
      horizontalList {
        val dataAtual = LocalDate.now().format()
        val horaAtual = LocalTime.now().format()
        val custno = notaSaida.custno
        val fornecedor = notaSaida.fornecedor
        val fornecedorSap = notaSaida.fornecedorSap
        val vendno = notaSaida.vendno
        val pedido = notaSaida.pedido
        val dataPedido = notaSaida.dataPedido.format()
        text("$custno - $fornecedor (FOR - $vendno  SAP - $fornecedorSap)   PED. $pedido - $dataPedido", LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
    }
  }

  private fun titleBuiderNota66(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("ENGECOPI ${notaSaida.sigla}", CENTER).apply {
          this.setStyle(fieldFontGrande)
        }
      }
      horizontalList {
        val dataAtual = LocalDate.now().format()
        val horaAtual = LocalTime.now().format()
        val custno = notaSaida.custno
        val fornecedor = notaSaida.fornecedor
        val vendno = notaSaida.vendno
        val pedido = notaSaida.pedido
        val dataPedido = notaSaida.dataPedido.format()
        val nota = notaSaida.nota
        val dataNota = notaSaida.dataNota.format()
        val fornecedorSap = notaSaida.fornecedorSap

        text("$custno - $fornecedor (FOR - $vendno  SAP - $fornecedorSap)   PED. $pedido - $dataPedido   NOTA $nota - $dataNota",
             LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
    }
  }

  private fun titleBuilderFinanceiro(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("ENGECOPI ${notaSaida.sigla}", CENTER).apply {
          this.setStyle(fieldFontGrande)
        }
      }
      horizontalList {
        val dataAtual = LocalDate.now().format()
        val horaAtual = LocalTime.now().format()
        val custno = notaSaida.custno
        val fornecedor = notaSaida.fornecedor
        val vendno = notaSaida.vendno
        val nota = notaSaida.nota
        val dataNota = notaSaida.dataNota.format()
        val fornecedorSap = notaSaida.fornecedorSap

        text("$custno - $fornecedor (FOR - $vendno  SAP - $fornecedorSap)   NFA $nota - $dataNota", LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
    }
  }

  private fun titleBuiderAjuste(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("ENGECOPI ${notaSaida.sigla}", CENTER).apply {
          this.setStyle(fieldFontGrande)
        }
      }
      horizontalList {
        val dataAtual = LocalDate.now().format()
        val horaAtual = LocalTime.now().format()
        val custno = notaSaida.custno
        val fornecedor = notaSaida.fornecedor
        val vendno = notaSaida.vendno
        val nota = notaSaida.nota
        val dataNota = notaSaida.dataNota.format()
        val fornecedorSap = notaSaida.fornecedorSap

        text("$custno - $fornecedor (FOR - $vendno  SAP - $fornecedorSap)   NFA $nota - $dataNota", LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
    }
  }

  private fun titleBuiderNota01(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("Natureza: ${notaSaida.natureza}", LEFT)
        text("ENGECOPI ${notaSaida.sigla}", CENTER).apply {
          this.setStyle(fieldFontGrande)
          this.setFixedWidth(120)
        }
        text("", RIGHT)
      }
      horizontalList {
        val dataAtual = LocalDate.now().format()
        val horaAtual = LocalTime.now().format()
        val custno = notaSaida.custno
        val fornecedor = notaSaida.fornecedor
        val vendno = notaSaida.vendno
        val nota = notaSaida.nota
        val dataNota = notaSaida.dataNota.format()
        val fatura = notaSaida.fatura
        val fornecedorSap = notaSaida.fornecedorSap

        text("$custno - $fornecedor (FOR - $vendno  SAP - $fornecedorSap)   NFD $nota - $dataNota   DUP $fatura", LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
    }
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return when (notaSaida.tipo) {
      "PED" -> titleBuiderPedido()
      "AJT" -> titleBuiderAjuste()
      "FIN" -> titleBuilderFinanceiro()
      "66"  -> titleBuiderNota66()
      else  -> titleBuiderNota01()
    }
  }

  private fun sumaryBuild(): ComponentBuilder<*, *> {
    return verticalBlock {
      if (notaSaida.tipo == "1") {
        breakLine()
        val widitInicial = 110
        this.horizontalList {
          this.verticalList {
            setStyle(fieldBorder)
            text("BASE DE CÁLCULO DO ICMS", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.baseIcms.format(), RIGHT) { this.setStyle(fieldFont) }
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR DO ICMS", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.valorIcms.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("BASE DE CÁLCULO DO ICMS SUBSTITUIÇÃO", LEFT) {
              this.setStyle(fieldFontTitle)
            }
            text(notaSaida.baseIcmsSubst.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR ICMS SUBSTITUIÇÃO", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.icmsSubst.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR TOTAL DOS PRODUTOS", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.valorTotalProduto.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial)
          }
        }
        this.horizontalList {
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR DO FRETE", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.valorFrete.format(), RIGHT) { this.setStyle(fieldFont) }
          }

          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR DO SEGURO", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.valorSeguro.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial * 2 * 3 / 8)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("DESCONTO", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.valorDesconto.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial * 2 * 2 / 8)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR ICMS SUBSTITUIÇÃO", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.icmsSubst.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial * 2 * 3 / 8)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("OUTRAS DESPESAS ACESSÓRIAS", LEFT) { this.setStyle(fieldFontTitle) }
            text(notaSaida.outrasDespesas.format(), RIGHT) { this.setStyle(fieldFont) }
            setWidth(widitInicial)
          }
          this.verticalList {
            setStyle(fieldBorder)
            text("VALOR TOTAL DA NOTA", LEFT) {
              this.setStyle(stl.style(fieldFontTitle).bold())
            }
            text(notaSaida.valorTotal.format(), RIGHT) {
              this.setStyle(stl.style(fieldFont).bold())
            }
            setWidth(widitInicial)
          }
        }
      }
      breakLine()

      text("OBSERVAÇÕES:", LEFT, 100)
      text(notaSaida.obsNota, LEFT)
      if (notaSaida.tipo in listOf("66", "PED", "AJT", "FIN")) text(notaSaida.obsPedido)
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return if (notaSaida.tipo in listOf("66", "PED", "AJT", "FIN")) listOf(
      sbt.text("Total R$", valorUnitarioCol),
      sbt.sum(valorTotalCol),
      sbt.sum(ipiCol),
      sbt.sum(vstCol),
      sbt.sum(valorTotalIpiCol),
                                                                          )
    else listOf(
      sbt.text("Total R$", valorUnitarioCol),
      sbt.sum(valorTotalCol),
      sbt.sum(ipiCol),
      sbt.sum(vstCol),
      sbt.sum(valorTotalIpiCol),
               )
  }

  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    var index = 1
    val itens = notaSaida.listaProdutos().sortedBy { it.codigo.toIntOrNull() ?: 0 }.map {
      it.apply {
        item = index++
      }
    }
    val pageOrientation = if ((notaSaida.tipo in listOf("66", "PED", "AJT", "FIN")) && resumida) PORTRAIT
    else LANDSCAPE
    return report().title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(itens)
      .summary(sumaryBuild())
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
  }

  companion object {
    fun processaRelatorio(listNota: List<NotaSaida>, resumida: Boolean = false): ByteArray {
      val printList = listNota.map { nota ->
        val report = RelatorioNotaDevolucao(nota, resumida).makeReport()
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
