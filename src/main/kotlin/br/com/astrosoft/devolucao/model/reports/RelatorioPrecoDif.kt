package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT
import net.sf.dynamicreports.report.constant.TextAdjust

class RelatorioPrecoDif(val notas: List<NfPrecEntrada>) : ReportBuild<NfPrecEntrada>() {
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
    columnString(NfPrecEntrada::grade, width = 45, aligment = CENTER, title = "Grade")
    columnDouble(NfPrecEntrada::precon, width = 40, title = "R$ NF")
    columnDouble(NfPrecEntrada::precop, width = 40, title = "R$ Ped")
    columnDouble(NfPrecEntrada::precopc, width = 40, title = "R$ Prec")
    columnDouble(NfPrecEntrada::precoDifValue, width = 40, title = "Dif")
    columnDouble(NfPrecEntrada::precoPercen, width = 40, title = "%")
  }

  override val propriedades =
    PropriedadeRelatorio(titulo = "Diferença de preço", subTitulo = "", detailFonteSize = 8, pageOrientation = LANDSCAPE)

  override fun listDataSource(): List<NfPrecEntrada> = notas

  companion object {
    fun processaRelatorio(notas: List<NfPrecEntrada>): ByteArray {
      val report = RelatorioPrecoDif(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
