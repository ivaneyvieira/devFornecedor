package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntradaGrupo
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import br.com.astrosoft.framework.model.reports.Templates.columnTitleStyle
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.margin
import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.style.Styles.padding
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.TextAdjust
import java.awt.Color

class RelatorioUltimasNotasGrupo(val notas: List<UltimaNotaEntradaGrupo>) : ReportBuild<UltimaNotaEntradaGrupo>() {
  init {
    columnString(UltimaNotaEntradaGrupo::dataStr, width = 80, title = "Data")
    columnString(UltimaNotaEntradaGrupo::nfe, width = 80, aligment = CENTER, title = "NF")
    columnString(UltimaNotaEntradaGrupo::prod, width = 60, aligment = CENTER, title = "Prod")
    columnString(UltimaNotaEntradaGrupo::descricao, title = "Descrição", width = 180) {
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }
    columnString(UltimaNotaEntradaGrupo::valorNota, title = "NF", width = 80, aligment = RIGHT) {
      this.setStyle(stl.style().setForegroundColor(Color.YELLOW))
    }
    columnString(UltimaNotaEntradaGrupo::valorPrecificacao, title = "Prec", width = 80, aligment = RIGHT) {
      this.setStyle(stl.style().setForegroundColor(Color.YELLOW))
    }
  }

  override fun makeReport(): JasperReportBuilder {
    return super.makeReport()
      .setPageMargin(margin(0))
      .setTitleStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setTop(20)))
      .setColumnStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setLeft(20)))
      .setGroupStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setLeft(20)))
      //.setColumnTitleStyle(columnTitleStyle.setPadding(padding().setLeft(20)))
      //.setDetailStyle(stl.style().setForegroundColor(Color.WHITE).setPadding(padding().setLeft(20)))
      .setBackgroundStyle(stl.style().setBackgroundColor(Color(35, 51, 72)).setPadding(padding(20)))
  }

  override fun titleBuider(): ComponentBuilder<*, *> {
    return super.titleBuider()
  }

  override fun labelTitleCol() = columnString(UltimaNotaEntradaGrupo::nomeGrupo)

  override val propriedades =
          PropriedadeRelatorio(titulo = "NF x Precificação",
                               subTitulo = "",
                               color = Color.WHITE,
                               pageOrientation = PORTRAIT)

  override fun listDataSource(): List<UltimaNotaEntradaGrupo> = notas

  companion object {
    fun processaRelatorio(notas: List<UltimaNotaEntradaGrupo>): ByteArray {
      val report = RelatorioUltimasNotasGrupo(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
