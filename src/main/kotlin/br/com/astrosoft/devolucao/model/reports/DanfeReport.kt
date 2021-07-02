package br.com.astrosoft.devolucao.model.reports

import br.com.astrosoft.framework.util.SystemUtils.readStream
import br.com.astrosoft.devolucao.model.ItensNotaReport
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

object DanfeReport {
  fun create(itens : List<ItensNotaReport>) : ByteArray {
    val jasperFile = "/report/projeto/notafiscal.jasper"
    val jasperInputStream = readStream(jasperFile)
    val parameter = hashMapOf<String, Any>()
    val collection = JRBeanCollectionDataSource(itens)

    val print = JasperFillManager.fillReport(jasperInputStream, parameter, collection)

    return JasperExportManager.exportReportToPdf(print) ?: ByteArray(0)
  }
}