package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.TextAdjust

class RelatorioTodasEntradas(val notas: List<NotaEntradaQuery>) : ReportBuild<NotaEntradaQuery>() {
  init {
    columnInt(NotaEntradaQuery::lj, width = 20, aligment = CENTER, title = "Lj")
    columnString(NotaEntradaQuery::dataStr, width = 50, title = "Data")
    columnString(NotaEntradaQuery::nfe, width = 50, aligment = CENTER, title = "NF")
    columnInt(NotaEntradaQuery::fornCad, width = 40, aligment = CENTER, title = "F Cad")
    columnString(NotaEntradaQuery::prod, width = 45, aligment = CENTER, title = "Prod")
    columnString(NotaEntradaQuery::descricao, title = "Descrição") {
      this.setTextAdjust(TextAdjust.SCALE_FONT)
    }
    columnDouble(NotaEntradaQuery::aliqIpi, width = 40, title = "IPI N")
    columnDouble(NotaEntradaQuery::aliqIpiP, width = 40, title = "IPI P")
    columnDouble(NotaEntradaQuery::aliqIcms, width = 40, title = "ICMS N")
    columnDouble(NotaEntradaQuery::aliqIcmsP, width = 40, title = "ICMS P")
  }

  override val propriedades =
      PropriedadeRelatorio(titulo = "Entradas", subTitulo = "", detailFonteSize = 8, pageOrientation = PORTRAIT)

  override fun listDataSource(): List<NotaEntradaQuery> = notas

  companion object {
    fun processaRelatorio(notas: List<NotaEntradaQuery>): ByteArray {
      val report = RelatorioTodasEntradas(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
