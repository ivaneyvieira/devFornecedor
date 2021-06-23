package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntradaGrupo
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.TextAdjust
import java.awt.Color

class RelatorioUltimasNotasGrupo(val notas: List<UltimaNotaEntradaGrupo>) : ReportBuild<UltimaNotaEntradaGrupo>() {
  val icmsDif = columnString(UltimaNotaEntradaGrupo::icmsDif, oculto = true)
  val ipiDif = columnString(UltimaNotaEntradaGrupo::ipiDif, oculto = true)
  val mvaDif = columnString(UltimaNotaEntradaGrupo::mvaDif, oculto = true)
  val ncmDif = columnString(UltimaNotaEntradaGrupo::ncmDif, oculto = true)
  val cstDif = columnString(UltimaNotaEntradaGrupo::cstDif, oculto = true)

  val condicaoIcms = stl.conditionalStyle(cnd.equal(icmsDif, "N")).setForegroundColor(Color.YELLOW)
  val condicaoIpi = stl.conditionalStyle(cnd.equal(ipiDif, "N")).setForegroundColor(Color.YELLOW)
  val condicaoMva = stl.conditionalStyle(cnd.equal(mvaDif, "N")).setForegroundColor(Color.YELLOW)
  val condicaoNcm = stl.conditionalStyle(cnd.equal(ncmDif, "N")).setForegroundColor(Color.YELLOW)
  val condicaoCst = stl.conditionalStyle(cnd.equal(cstDif, "N")).setForegroundColor(Color.YELLOW)

  init {
    columnString(UltimaNotaEntradaGrupo::dataStr, width = 65, title = "Data")
    columnString(UltimaNotaEntradaGrupo::nfe, width = 60, aligment = CENTER, title = "NF")
    columnString(UltimaNotaEntradaGrupo::prod, width = 45, aligment = CENTER, title = "Prod")
    columnString(UltimaNotaEntradaGrupo::descricao, title = "Descrição", width = 200) {
      this.setTextAdjust(TextAdjust.CUT_TEXT)
    }
    columnDouble(UltimaNotaEntradaGrupo::icmsn, width = 40) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoIcms))
    }
    columnDouble(UltimaNotaEntradaGrupo::icmsp, width = 40) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoIcms))
    }
    columnDouble(UltimaNotaEntradaGrupo::ipin, width = 40) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoIpi))
    }
    columnDouble(UltimaNotaEntradaGrupo::ipip, width = 40) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoIpi))
    }
    columnString(UltimaNotaEntradaGrupo::cstn, width = 30, aligment = CENTER) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoCst))
    }
    columnString(UltimaNotaEntradaGrupo::cstp, width = 30, aligment = CENTER) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoCst))
    }
    columnDouble(UltimaNotaEntradaGrupo::mvan, width = 40) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoMva))
    }
    columnDouble(UltimaNotaEntradaGrupo::mvap, width = 40) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoMva))
    }
    columnString(UltimaNotaEntradaGrupo::ncmn, width = 55, aligment = CENTER) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoNcm))
    }
    columnString(UltimaNotaEntradaGrupo::ncmp, width = 55, aligment = CENTER) {
      this.setStyle(stl.style().setForegroundColor(Color.WHITE).conditionalStyles(condicaoNcm))
    }
  }

  override fun makeReport(): JasperReportBuilder {
    return super.makeReport()
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(Color.WHITE))
      .setColumnStyle(stl.style().setForegroundColor(Color.WHITE))
      .setGroupStyle(stl.style().setForegroundColor(Color.WHITE))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)))
  }

  override fun labelTitleCol() = columnString(UltimaNotaEntradaGrupo::nomeGrupo)

  override val propriedades =
          PropriedadeRelatorio(titulo = "NF x Precificação",
                               subTitulo = "",
                               color = Color.WHITE,
                               pageOrientation = LANDSCAPE)

  override fun listDataSource(): List<UltimaNotaEntradaGrupo> = notas

  companion object {
    fun processaRelatorio(notas: List<UltimaNotaEntradaGrupo>): ByteArray {
      val report = RelatorioUltimasNotasGrupo(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
