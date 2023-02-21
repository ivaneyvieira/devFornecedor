package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.NfEntradaFrete
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE

class RelatorioCte(val notas: List<NfEntradaFrete>) : ReportBuild<NfEntradaFrete>() {
  init {
    columnInt(NfEntradaFrete::loja, width = 20, aligment = CENTER, title = "Loja")
    columnInt(NfEntradaFrete::vendno, width = 35, aligment = CENTER, title = "Forn")
    columnDouble(NfEntradaFrete::totalPrd, width = 50, title = "R$ Prd")
    columnDouble(NfEntradaFrete::valorNF, width = 50, title = "R$ NF")
    columnInt(NfEntradaFrete::carrno, width = 35, aligment = CENTER, title = "Transp")
    columnString(NfEntradaFrete::carrName, width = 0, aligment = LEFT, title = "Nome")
    columnInt(NfEntradaFrete::cte, width = 40, title = "Cte", aligment = CENTER)
    columnString(NfEntradaFrete::dataStr, width = 45, title = "Entrada", aligment = CENTER)
    columnDouble(NfEntradaFrete::valorCte, width = 50, title = "R$ Frete Fat")
    columnDouble(NfEntradaFrete::totalFrete, width = 50, title = "R$ Frete Cal")
    columnDouble(NfEntradaFrete::pesoBruto, width = 50, title = "P Bruto")
    columnDouble(NfEntradaFrete::pesoCub, width = 35, title = "Peso Cub")
    columnDouble(NfEntradaFrete::cub, width = 30, title = "Cub")
    columnDouble(NfEntradaFrete::fretePeso, width = 40, title = "R$ F Peso")
    columnDouble(NfEntradaFrete::adValore, width = 30, title = "R$ Adv")
    columnDouble(NfEntradaFrete::gris, width = 30, title = "R$ Gris")
    columnDouble(NfEntradaFrete::taxa, width = 30, title = "Taxa")
    columnDouble(NfEntradaFrete::outro, width = 30, title = "Outros")
    columnDouble(NfEntradaFrete::outro, width = 30, title = "Aliqt")
    columnDouble(NfEntradaFrete::icms, width = 30, title = "ICMS")
  }

  override val propriedades =
    PropriedadeRelatorio(titulo = "Nota Frete", subTitulo = "", detailFonteSize = 8, pageOrientation = LANDSCAPE)

  override fun listDataSource(): List<NfEntradaFrete> = notas

  companion object {
    fun processaRelatorio(notas: List<NfEntradaFrete>): ByteArray {
      val report = RelatorioCte(notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
