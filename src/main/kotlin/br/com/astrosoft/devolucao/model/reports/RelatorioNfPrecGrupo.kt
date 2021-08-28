package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NfPrecEntradaGrupo
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.margin
import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.style.Styles.padding
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.TextAdjust
import java.awt.Color

class RelatorioNfPrecGrupo(val notas: List<NfPrecEntradaGrupo>) : ReportBuild<NfPrecEntradaGrupo>() {
  init {
    columnString(NfPrecEntradaGrupo::dataStr, width = 80, title = "Data")
    columnString(NfPrecEntradaGrupo::nfe, width = 80, aligment = CENTER, title = "NF")
    columnString(NfPrecEntradaGrupo::prod, width = 60, aligment = CENTER, title = "Prod")
    columnString(NfPrecEntradaGrupo::descricao, title = "Descrição", width = 180) {
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }
    columnString(NfPrecEntradaGrupo::valorNota, title = "NF", width = 80, aligment = RIGHT) {
      this.setStyle(stl.style().setForegroundColor(Color.YELLOW))
    }
    columnString(NfPrecEntradaGrupo::valorPrecificacao, title = "Prec", width = 80, aligment = RIGHT) {
      this.setStyle(stl.style().setForegroundColor(Color.YELLOW))
    }
  }

  override fun makeReport(): JasperReportBuilder {
    return super.makeReport()
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setTop(20)))
      .setColumnStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setLeft(20)))
      .setGroupStyle(stl.style()
                       .setForegroundColor(Color.WHITE)
                       .setPadding(padding().setLeft(20)))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)).setPadding(padding(20)))
  }

  override fun labelTitleCol() = columnString(NfPrecEntradaGrupo::nomeGrupo)

  override val propriedades =
          PropriedadeRelatorio(titulo = "NF x Precificação",
                               subTitulo = "",
                               color = Color.WHITE,
                               pageOrientation = PORTRAIT)

  override fun listDataSource(): List<NfPrecEntradaGrupo> = notas

  companion object {
    fun processaRelatorio(notas: List<NfPrecEntradaGrupo>): ByteArray {
      val report = RelatorioNfPrecGrupo(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
