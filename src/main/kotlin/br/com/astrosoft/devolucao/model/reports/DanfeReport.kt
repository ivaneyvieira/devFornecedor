package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.devolucao.model.ItensNotaReport
import br.com.astrosoft.framework.util.SystemUtils.readStream
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

object DanfeReport {
  private val jasperReport = compileReport()

  fun create(itens: List<ItensNotaReport>): ByteArray {
    val printReport = fillReport(itens)
    return JasperExportManager.exportReportToPdf(printReport) ?: ByteArray(0)
  }

  private fun fillReport(itens: List<ItensNotaReport>): JasperPrint? {
    val parameter = hashMapOf<String, Any>()
    val collection = JRBeanCollectionDataSource(itens)
    return JasperFillManager.fillReport(jasperReport, parameter, collection)
  }

  private fun compileReport(): JasperReport {
    val jasperFile = "/report/projeto/notafiscal.jrxml"
    val jasperInputStream = readStream(jasperFile)
    return JasperCompileManager.compileReport(jasperInputStream)
  }
}