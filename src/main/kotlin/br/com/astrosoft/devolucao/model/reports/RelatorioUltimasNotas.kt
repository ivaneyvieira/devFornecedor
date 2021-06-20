package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.TextAdjust

class RelatorioUltimasNotas(val notas: List<UltimaNotaEntrada>) : ReportBuild<UltimaNotaEntrada>() {

  init {
    columnInt(UltimaNotaEntrada::lj, width = 20, aligment = CENTER, title = "Lj")
    columnInt(UltimaNotaEntrada::ni, width = 45, title = "NI")
    columnString(UltimaNotaEntrada::dataStr, width = 65, title = "Data")
    columnString(UltimaNotaEntrada::nfe, width = 50, aligment = CENTER, title = "NF")
    columnString(UltimaNotaEntrada::forn, width = 35, aligment = RIGHT, title = "Forn")
    columnString(UltimaNotaEntrada::prod, width = 45, aligment = CENTER, title = "Prod")
    columnString(UltimaNotaEntrada::descricao, title = "Descrição") {
      this.setTextAdjust(TextAdjust.CUT_TEXT)
    }
    columnDouble(UltimaNotaEntrada::icmsn, width = 40)
    columnDouble(UltimaNotaEntrada::icmsp, width = 40)
    columnDouble(UltimaNotaEntrada::ipin, width = 40)
    columnDouble(UltimaNotaEntrada::ipip, width = 40)
    columnString(UltimaNotaEntrada::cstn, width = 30, aligment = CENTER)
    columnString(UltimaNotaEntrada::cstp, width = 30, aligment = CENTER)
    columnDouble(UltimaNotaEntrada::mvan, width = 40)
    columnDouble(UltimaNotaEntrada::mvap, width = 40)
    columnString(UltimaNotaEntrada::ncmn, width = 55, aligment = CENTER)
    columnString(UltimaNotaEntrada::ncmp, width = 55, aligment = CENTER)
  }

  override val propriedades =
          PropriedadeRelatorio(titulo = "NF x Precificação", subTitulo = "", pageOrientation = LANDSCAPE)

  override fun listDataSource(): List<UltimaNotaEntrada> = notas

  companion object {
    fun processaRelatorio(notas: List<UltimaNotaEntrada>): ByteArray {
      val report = RelatorioUltimasNotas(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
