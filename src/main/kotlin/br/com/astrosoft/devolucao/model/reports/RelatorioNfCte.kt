package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NfFreteGrupo
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.margin
import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.style.Styles.padding
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import java.awt.Color

class RelatorioNfCte(val notas: List<NfFreteGrupo>) : ReportBuild<NfFreteGrupo>() {
  init {
    columnInt(NfFreteGrupo::cte, width = 40, title = "Cte")
    columnString(NfFreteGrupo::dataStr, width = 60, title = "Data", aligment = CENTER)
    columnString(NfFreteGrupo::nfe, width = 0, aligment = LEFT, title = "NF")
    columnString(NfFreteGrupo::valorNota, title = "R\$ Frete Fat", width = 60, aligment = RIGHT) {
      this.setStyle(stl.style().setForegroundColor(Color.YELLOW).setFontSize(8))
    }
    columnString(NfFreteGrupo::valorCalculado, title = "R\$ Frete Cal", width = 60, aligment = RIGHT) {
      this.setStyle(stl.style().setForegroundColor(Color.YELLOW).setFontSize(8))
    }
  }

  override fun makeReport(): JasperReportBuilder {
    return super
      .makeReport()
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setTop(20)))
      .setColumnStyle(stl.style().setForegroundColor(Color.WHITE).setFontSize(8))
      .setGroupStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setLeft(4)))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)))
  }

  override fun labelTitleCol() = columnString(NfFreteGrupo::nomeGrupo)

  override val propriedades =
    PropriedadeRelatorio(
      titulo = "CTe",
      subTitulo = "",
      color = Color.WHITE,
      detailFonteSize = 8,
      pageOrientation = PORTRAIT
    )

  override fun listDataSource(): List<NfFreteGrupo> = notas

  companion object {
    fun processaRelatorio(notas: List<NfFreteGrupo>): ByteArray {
      val report = RelatorioNfCte(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
