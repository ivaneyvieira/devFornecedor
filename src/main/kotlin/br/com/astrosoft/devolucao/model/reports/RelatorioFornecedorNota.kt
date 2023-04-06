package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.beans.FornecedorNota
import br.com.astrosoft.framework.model.reports.PropriedadeRelatorio
import br.com.astrosoft.framework.model.reports.ReportBuild
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.PageOrientation.PORTRAIT

class RelatorioFornecedorNota(val subTitulo: String, val notas: List<FornecedorNota>) : ReportBuild<FornecedorNota>() {
  init {
    columnInt(FornecedorNota::loja, width = 20, aligment = CENTER, title = "Loja")
    columnInt(FornecedorNota::ni, width = 40, aligment = CENTER, title = "NI")
    columnString(FornecedorNota::nf, width = 40, aligment = LEFT, title = "NF")
    columnLocalDate(FornecedorNota::emissao, width = 50, aligment = CENTER, title = "Emissão")
    columnLocalDate(FornecedorNota::entrada, width = 50, aligment = CENTER, title = "Entrada")
    columnLocalDate(FornecedorNota::vencimento, width = 50, aligment = CENTER, title = "Vencimento")
    columnString(FornecedorNota::obs, width = 0, aligment = LEFT, title = "Observação")
    columnString(FornecedorNota::situacao, width = 60, aligment = CENTER, title = "Situação")
  }

  override val propriedades =
    PropriedadeRelatorio(
      titulo = "Notas do Fornecedor",
      subTitulo = subTitulo,
      detailFonteSize = 8,
      pageOrientation = PORTRAIT
    )

  override fun listDataSource(): List<FornecedorNota> = notas

  companion object {
    fun processaRelatorio(subTitulo: String, notas: List<FornecedorNota>): ByteArray {
      val report = RelatorioFornecedorNota(subTitulo, notas).makeReport()
      val printList = listOf(report.toJasperPrint())
      return renderReport(printList)
    }
  }
}
