package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.TextAdjust

class RelatorioNfPrec(val notas: List<NfPrecEntrada>) : ReportBuild<NfPrecEntrada>() {

  init {
    columnInt(NfPrecEntrada::lj, width = 20, aligment = CENTER, title = "Lj")
    columnInt(NfPrecEntrada::ni, width = 45, title = "NI")
    columnString(NfPrecEntrada::dataStr, width = 65, title = "Data")
    columnString(NfPrecEntrada::nfe, width = 50, aligment = CENTER, title = "NF")
    columnString(NfPrecEntrada::fornCad, width = 35, aligment = RIGHT, title = "Forn")
    columnString(NfPrecEntrada::prod, width = 45, aligment = CENTER, title = "Prod")
    columnString(NfPrecEntrada::descricao, title = "Descrição") {
      this.setTextAdjust(TextAdjust.CUT_TEXT)
    }
    columnDouble(NfPrecEntrada::icmsn, width = 40)
    columnDouble(NfPrecEntrada::icmsp, width = 40)
    columnDouble(NfPrecEntrada::ipin, width = 40)
    columnDouble(NfPrecEntrada::ipip, width = 40)
    columnString(NfPrecEntrada::cstn, width = 30, aligment = CENTER)
    columnString(NfPrecEntrada::cstp, width = 30, aligment = CENTER)
    columnDouble(NfPrecEntrada::mvan, width = 40)
    columnDouble(NfPrecEntrada::mvap, width = 40)
    columnString(NfPrecEntrada::ncmn, width = 55, aligment = CENTER)
    columnString(NfPrecEntrada::ncmp, width = 55, aligment = CENTER)
  }

  override val propriedades =
          PropriedadeRelatorio(titulo = "NF x Precificação", subTitulo = "", pageOrientation = LANDSCAPE)

  override fun listDataSource(): List<NfPrecEntrada> = notas

  companion object {
    fun processaRelatorio(notas: List<NfPrecEntrada>): ByteArray {
      val report = RelatorioNfPrec(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
