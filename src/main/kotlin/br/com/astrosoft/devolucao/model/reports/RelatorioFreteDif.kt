package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.TextAdjust

class RelatorioFreteDif(val notas: List<NfPrecEntrada>) : ReportBuild<NfPrecEntrada>() {
  init {
    columnInt(NfPrecEntrada::lj, width = 20, aligment = CENTER, title = "Lj")
    columnInt(NfPrecEntrada::ni, width = 45, title = "NI")
    columnString(NfPrecEntrada::dataStr, width = 65, title = "Data", aligment = CENTER)
    columnString(NfPrecEntrada::nfe, width = 50, aligment = CENTER, title = "NF")
    columnString(NfPrecEntrada::fornCad, width = 35, aligment = RIGHT, title = "Forn")
    columnString(NfPrecEntrada::prod, width = 45, aligment = CENTER, title = "Prod")
    columnString(NfPrecEntrada::descricao, title = "Descrição") {
      this.setTextAdjust(TextAdjust.CUT_TEXT)
    }
    columnDouble(NfPrecEntrada::frete, width = 40)
    columnDouble(NfPrecEntrada::freten, width = 40)
    columnDouble(NfPrecEntrada::fretep, width = 40)
  }

  override val propriedades =
      PropriedadeRelatorio(
        titulo = "Diferença de frete",
        subTitulo = "",
        detailFonteSize = 8,
        pageOrientation = PORTRAIT
      )

  override fun listDataSource(): List<NfPrecEntrada> = notas

  companion object {
    fun processaRelatorio(notas: List<NfPrecEntrada>): ByteArray {
      val report = RelatorioFreteDif(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
