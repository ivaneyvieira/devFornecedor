package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.model.reports.Templates
import br.com.astrosoft.framework.model.reports.Templates.fieldFontGrande
import br.com.astrosoft.framework.model.reports.horizontalList
import br.com.astrosoft.framework.model.reports.text
import br.com.astrosoft.framework.model.reports.verticalBlock
import br.com.astrosoft.framework.util.format
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.style.Styles
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust.CUT_TEXT
import net.sf.dynamicreports.report.constant.TextAdjust.SCALE_FONT
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.awt.Color
import java.io.ByteArrayOutputStream

class RelatorioNotaDevFornecedor(val notaSaida: NotaSaida) {
  private val codigoCol: TextColumnBuilder<String> =
          col.column("Cód Forn", ProdutosNotaSaida::refFor.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setTextAdjust(SCALE_FONT)
            this.setFixedWidth(100)
          }
  private val dataInvCol: TextColumnBuilder<String> =
          col.column("Emissão", ProdutosNotaSaida::dateInvStr.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setTextAdjust(SCALE_FONT)
            this.setFixedWidth(40)
          }
  private val notaInvCol: TextColumnBuilder<String> =
          col.column("NF", ProdutosNotaSaida::notaInv.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setTextAdjust(SCALE_FONT)
            this.setFixedWidth(40)
          }
  private val ncmInvCol: TextColumnBuilder<String> =
          col.column("NCM", ProdutosNotaSaida::ncm.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setTextAdjust(SCALE_FONT)
            this.setFixedWidth(50)
          }
  private val refForCol: TextColumnBuilder<String> =
          col.column("Ref do Fab", ProdutosNotaSaida::refFor.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setTextAdjust(SCALE_FONT)
            this.setFixedWidth(60)
          }
  private val descricaoCol: TextColumnBuilder<String> =
          col.column("Descrição", ProdutosNotaSaida::refName.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(LEFT)
            this.setTextAdjust(CUT_TEXT)
          }
  private val gradeCol: TextColumnBuilder<String> =
          col.column("Grade", ProdutosNotaSaida::grade.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setTextAdjust(SCALE_FONT)
            this.setFixedWidth(40)
          }
  private val stCol: TextColumnBuilder<String> = col.column("ST", ProdutosNotaSaida::st.name, type.stringType()).apply {
    this.setHorizontalTextAlignment(CENTER)
    this.setFixedWidth(25)
  }
  private val invnoCol: TextColumnBuilder<String> =
          col.column("NI", ProdutosNotaSaida::invnoObs.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("0")
            this.setFixedWidth(30)
          }
  private val qtdeCol: TextColumnBuilder<Int> =
          col.column("Quant", ProdutosNotaSaida::qtde.name, type.integerType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("0")
            this.setFixedWidth(60)
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
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val valorTotalCol: TextColumnBuilder<Double> =
          col.column("V. Total", ProdutosNotaSaida::valorTotal.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val valorTotalIpiCol: TextColumnBuilder<Double> =
          col.column("R$ Total Geral", ProdutosNotaSaida::valorTotalIpi.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val ipiCol: TextColumnBuilder<Double> =
          col.column("Valor Ipi", ProdutosNotaSaida::ipi.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val vstCol: TextColumnBuilder<Double> =
          col.column("Valor ST", ProdutosNotaSaida::vst.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val baseICMSCol: TextColumnBuilder<Double> =
          col.column("B. Cálc. ICMS", ProdutosNotaSaida::baseICMS.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val valorICMSCol: TextColumnBuilder<Double> =
          col.column("Valor ICMS", ProdutosNotaSaida::valorICMS.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val valorIPICol: TextColumnBuilder<Double> =
          col.column("Valor IPI", ProdutosNotaSaida::valorIPI.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val aliqICMSCol: TextColumnBuilder<Double> =
          col.column("Alíq. ICMS", ProdutosNotaSaida::icmsAliq.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
          }
  private val aliqIPICol: TextColumnBuilder<Double> =
          col.column("Alíq. IPI", ProdutosNotaSaida::ipiAliq.name, type.doubleType()).apply {
            this.setHorizontalTextAlignment(RIGHT)
            this.setTextAdjust(SCALE_FONT)
            this.setPattern("#,##0.00")
            this.setFixedWidth(40)
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
            this.setFixedWidth(60)
          }

  private val ocorrenciaCol: TextColumnBuilder<String> =
          col.column("Ocorrência", ProdutosNotaSaida::ocorrencia.name, type.stringType()).apply {
            this.setHorizontalTextAlignment(CENTER)
            this.setFixedWidth(300)
          }

  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      emptyCol(),
      codigoCol,
      descricaoCol,
      ncmInvCol,
      unCol,
      qtdeCol,
      ocorrenciaCol,
      emptyCol(),
                 )
  }

  private fun emptyCol() = col.emptyColumn().apply {
    this.setFixedWidth(10)
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    val notaOrigem = notaSaida.notaOrigem()
    val notaOrigemNDD = notaOrigem?.itensNotaReport()?.firstOrNull()
    val wdNF = 150
    val wdCnpj = 150
    val wdEmissao = 70

    return verticalBlock {
      horizontalList {
        text("Notificação de Irregularidade no Recebimento", CENTER).apply {
          this.setStyle(fieldFontGrande)
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
      }
      horizontalList {
        val nome = notaOrigemNDD?.nomeEmitente ?: ""
        val cnpj = notaOrigemNDD?.cpnjEmitente ?: ""
        val notaFiscal = notaOrigem?.notaFiscal ?: ""
        val emissao = notaOrigem?.dataEmissao.format()
        text("", LEFT, 10)
        text("Fornecedor: $nome", LEFT).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("CNPJ: $cnpj", LEFT, wdCnpj).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("NF de Origem: $notaFiscal", LEFT, wdNF).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("Emissão:", RIGHT, wdEmissao).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text(emissao, RIGHT, wdEmissao).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("", LEFT, 10)
      }
      horizontalList {
        val nome = notaOrigemNDD?.nomeTransportadora ?: ""
        val cnpj = notaOrigemNDD?.cnpjCPF ?: ""
        val conhecimentoFrete = notaOrigem?.conhecimentoFrete
        val emissao = ""
        text("", LEFT, 10)
        text("Transportadora: $nome", LEFT).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("CNPJ: $cnpj", LEFT, wdCnpj).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("Cte: $conhecimentoFrete", LEFT, wdNF).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("Emissão:", RIGHT, wdEmissao).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text(emissao, RIGHT, wdEmissao).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("", LEFT, 10)
      }
      horizontalList {
        val nome = notaSaida.fornecedor
        val cnpj = ""
        val notaFiscal = notaSaida.nota
        val emissao = notaSaida.dataNota.format()
        text("", LEFT, 10)
        text("Cliente: $nome", LEFT).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("CNPJ: $cnpj", LEFT, wdCnpj).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("NF Devolução: $notaFiscal", LEFT, wdNF).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("Emissão:", RIGHT, wdEmissao).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text(emissao, RIGHT, wdEmissao).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("", LEFT, 10)
      }
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return emptyList()
  }

  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    var index = 1
    val itens = notaSaida.listaProdutos().sortedBy { it.codigo.toIntOrNull() ?: 0 }.map {
      it.apply {
        item = index++
      }
    }
    val pageOrientation = LANDSCAPE
    return report().title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .setColumnStyle(stl.style().setFontSize(7))
      .columnGrid(* colunms)
      .setDataSource(itens)
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setFontSize(8).setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
      .setColumnStyle(Templates.fieldFontNormal.setForegroundColor(Color.WHITE)
                        .setFontSize(8)
                        .setPadding(stl.padding().setRight(4).setLeft(4)))
      .setColumnTitleStyle(Templates.fieldFontNormalCol.setForegroundColor(Color.BLACK).setFontSize(10))
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(Styles.padding().setTop(20)))
      .setGroupStyle(stl.style().setForegroundColor(Color.WHITE))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)).setPadding(Styles.padding(20)))
  }

  companion object {
    fun processaRelatorio(listNota: List<NotaSaida>): ByteArray {
      val printList = listNota.map { nota ->
        nota.findNotaOrigem()
        val report = RelatorioNotaDevFornecedor(nota).makeReport()
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

