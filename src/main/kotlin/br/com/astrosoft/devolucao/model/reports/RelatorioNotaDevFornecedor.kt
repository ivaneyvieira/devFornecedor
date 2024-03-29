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
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.TextAdjust.SCALE_FONT
import net.sf.dynamicreports.report.constant.TextAdjust.STRETCH_HEIGHT
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.awt.Color
import java.io.ByteArrayOutputStream

class RelatorioNotaDevFornecedor(val notaSaida: NotaSaida, val ocorrencias: List<String>) {
  private val margem = 20
  private val notaOrigem = notaSaida.notaOrigem()
  private val produtosOrigemNDD = notaOrigem?.itensNotaReport().orEmpty()
  private val notaOrigemNDD = produtosOrigemNDD.firstOrNull()

  private val codigoCol: TextColumnBuilder<String> =
      col.column("Cód Forn", ProdutosNotaSaida::refFor.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(100)
      }

  private val ncmInvCol: TextColumnBuilder<String> =
      col.column("NCM", ProdutosNotaSaida::ncm.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setTextAdjust(SCALE_FONT)
        this.setFixedWidth(70)
      }

  private val descricaoCol: TextColumnBuilder<String> =
      col.column("Descrição", ProdutosNotaSaida::descricaoFornecedor.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setTextAdjust(STRETCH_HEIGHT)
      }

  private val qtdeCol: TextColumnBuilder<Int> =
      col.column("Quant", ProdutosNotaSaida::qtde.name, type.integerType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setTextAdjust(SCALE_FONT)
        this.setPattern("0")
        this.setFixedWidth(60)
      }

  private val barcodeCol: TextColumnBuilder<String> =
      col.column("Cód Barra", ProdutosNotaSaida::barcode.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setFixedWidth(100)
      }

  private val unCol: TextColumnBuilder<String> =
      col.column("Unid", ProdutosNotaSaida::un.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(CENTER)
        this.setFixedWidth(60)
      }

  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      emptyCol(),
      codigoCol,
      barcodeCol,
      descricaoCol,
      ncmInvCol,
      unCol,
      qtdeCol,
      emptyCol(),
    )
  }

  private fun emptyCol() = col.emptyColumn().apply {
    this.setFixedWidth(margem)
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    val wdNF = 150
    val wdCnpj = 150
    val wdEmissao = 70

    return verticalBlock {
      horizontalList {
        text("Notificação de Irregularidade no Recebimento", CENTER).apply {
          this.setStyle(fieldFontGrande.setForegroundColor(Color.WHITE))
        }
      }
      horizontalList {
        text("")
      }
      horizontalList {
        val nome = notaOrigemNDD?.nomeEmitente ?: ""
        val cnpj = notaOrigemNDD?.cpnjEmitente ?: ""
        val notaFiscal = notaOrigem?.notaFiscal ?: ""
        val emissao = notaOrigem?.dataEmissao.format()
        text("", LEFT, margem)
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
        text("", LEFT, margem)
      }
      horizontalList {
        val nome = notaOrigemNDD?.nomeTransportadora ?: ""
        val cnpj = notaOrigemNDD?.cnpjCPF ?: ""
        val conhecimentoFrete = notaOrigem?.conhecimentoFrete
        val emissao = ""
        text("", LEFT, margem)
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
        text("", LEFT, margem)
      }
      horizontalList {
        val nome = notaOrigemNDD?.nomeDestinatario ?: ""
        val cnpj = notaOrigemNDD?.cnpjCpfDestinatario ?: ""
        val notaFiscal = notaSaida.nota
        val emissao = notaSaida.dataNota.format()
        text("", LEFT, margem)
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
        text("", LEFT, margem)
      }

    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("")
      }
      horizontalList {
        text("", LEFT, margem)
        text("Ocorrências: ", LEFT).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
          this.setFixedWidth(100)
        }
        text(ocorrencias.joinToString(", "), LEFT).apply {
          this.setStyle(stl.style().setForegroundColor(Color.WHITE))
        }
        text("", LEFT, margem)
      }
    }
  }

  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    var index = 1
    val itens = notaSaida.listaProdutos().sortedBy { it.codigo }.map {
      it.setProdutoNdd(produtosOrigemNDD)
      it.apply {
        item = index++
      }
    }
    val pageOrientation = LANDSCAPE
    return report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(itens.toList())
      .setPageFormat(A4, pageOrientation)
      .setPageMargin(margin(28))
      .summary(pageFooterBuilder())
      .setSummaryStyle(stl.style().setFontSize(10))
      .setDetailStyle(stl.style().setFontSize(10))
      .setSubtotalStyle(stl.style().setFontSize(10).setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(10)))
      .setColumnStyle(
        Templates.fieldFontNormal
          .setForegroundColor(Color.WHITE)
          .setFontSize(10)
          .setPadding(stl.padding().setRight(4).setLeft(4))
      )
      .setColumnTitleStyle(Templates.fieldFontNormalCol.setForegroundColor(Color.BLACK).setFontSize(10))
      .setPageMargin(margin(0))
      .setTitleStyle(
        stl
          .style()
          .setFontSize(10)
          .setForegroundColor(Color.WHITE)
          .setPadding(Styles.padding().setTop(20))
      )
      .setGroupStyle(stl.style().setForegroundColor(Color.WHITE))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)).setPadding(Styles.padding(20)))
  }

  companion object {
    fun processaRelatorio(listNota: List<NotaSaida>, ocorrencias: List<String>): ByteArray {
      val printList = listNota.map { nota ->
        nota.findNotaOrigem()
        val report = RelatorioNotaDevFornecedor(nota, ocorrencias).makeReport()
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

