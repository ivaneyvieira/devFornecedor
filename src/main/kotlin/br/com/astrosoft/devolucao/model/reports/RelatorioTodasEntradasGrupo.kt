package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NotaEntradaQueryGrupo
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.builder.style.Styles
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.TextAdjust
import java.awt.Color

class RelatorioTodasEntradasGrupo(val notas: List<NotaEntradaQueryGrupo>) : ReportBuild<NotaEntradaQueryGrupo>() {
  init {
    columnInt(NotaEntradaQueryGrupo::lj, width = 20, aligment = CENTER, title = "Lj")
    columnString(NotaEntradaQueryGrupo::dataStr, width = 50, title = "Data")
    columnString(NotaEntradaQueryGrupo::nfe, width = 50, aligment = CENTER, title = "NF")
    columnInt(NotaEntradaQueryGrupo::fornCad, width = 40, aligment = CENTER, title = "F Cad")
    columnString(NotaEntradaQueryGrupo::prod, width = 45, aligment = CENTER, title = "Prod")
    columnString(NotaEntradaQueryGrupo::descricao, width = 280, title = "Descrição") {
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }
    columnString(NotaEntradaQueryGrupo::valorNota, width = 40, aligment = CENTER, title = "Nota")
    columnString(NotaEntradaQueryGrupo::valorProduto, width = 40, aligment = CENTER, title = "Produto")
  }

  override val propriedades =
      PropriedadeRelatorio(
        titulo = "Entrada",
        subTitulo = "",
        color = Color.WHITE,
        detailFonteSize = 8,
        pageOrientation = PORTRAIT
      )

  override fun labelTitleCol() = columnString(NotaEntradaQueryGrupo::descricaoGrupo)

  override fun listDataSource() = notas

  override fun makeReport(): JasperReportBuilder {
    return super
      .makeReport()
      .setPageMargin(DynamicReports.margin(0))
      .setTitleStyle(
        DynamicReports.stl.style().setForegroundColor(Color.WHITE).setPadding(Styles.padding().setTop(20))
      )
      .setColumnStyle(DynamicReports.stl.style().setForegroundColor(Color.WHITE).setFontSize(8))
      .setGroupStyle(
        DynamicReports.stl.style().setForegroundColor(Color.WHITE).setPadding(Styles.padding().setLeft(4))
      )
      .setBackgroundStyle(DynamicReports.stl.style().setBackgroundColor(Color(35, 51, 72)))
  }

  companion object {
    fun processaRelatorio(notas: List<NotaEntradaQueryGrupo>): ByteArray {
      val report = RelatorioTodasEntradasGrupo(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
