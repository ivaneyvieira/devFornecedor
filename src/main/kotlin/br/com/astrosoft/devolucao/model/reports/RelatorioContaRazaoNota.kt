package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.ContaRazaoNota
import br.com.astrosoft.devolucao.model.beans.FornecedorNota
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT

class RelatorioContaRazaoNota(val subTitulo: String, val notas: List<ContaRazaoNota>) : ReportBuild<ContaRazaoNota>() {
  init {
    columnInt(ContaRazaoNota::loja, width = 20, aligment = CENTER, title = "Loja")
    columnInt(ContaRazaoNota::ni, width = 40, aligment = CENTER, title = "NI")
    columnString(ContaRazaoNota::nf, width = 40, aligment = LEFT, title = "NF")
    columnInt(ContaRazaoNota::vendno, width = 40, aligment = LEFT, title = "Forn")
    columnLocalDate(ContaRazaoNota::emissao, width = 50, aligment = CENTER, title = "Emissão")
    columnLocalDate(ContaRazaoNota::entrada, width = 50, aligment = CENTER, title = "Entrada")
    columnLocalDate(ContaRazaoNota::vencimento, width = 50, aligment = CENTER, title = "Vencimento")
    columnString(ContaRazaoNota::obs, width = 0, aligment = LEFT, title = "Observação")
    columnString(ContaRazaoNota::situacao, width = 60, aligment = CENTER, title = "Situação")
  }

  override val propriedades =
      PropriedadeRelatorio(
        titulo = "Notas do Fornecedor",
        subTitulo = subTitulo,
        detailFonteSize = 8,
        pageOrientation = PORTRAIT
      )

  override fun listDataSource(): List<ContaRazaoNota> = notas

  companion object {
    fun processaRelatorio(subTitulo: String, notas: List<ContaRazaoNota>): ByteArray {
      val report = RelatorioContaRazaoNota(subTitulo, notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
