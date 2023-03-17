package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaXML
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaEntradaXML
import br.com.astrosoft.devolucao.model.reports.DanfeReport
import br.com.astrosoft.devolucao.model.reports.ETIPO_COPIA
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class TabFileNFEViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabFileNFEViewModel

  val list = mutableListOf<NotaEntradaXML>()

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroNotaEntradaXML): List<NotaEntradaXML> {
    return NotaEntradaXML.findAll(filtro)
  }

  fun updateViewBD() {
    val filter = subView.getFiltro()
    val listBD = NotaEntradaXML.findAll(filter)
    list.clear()
    list.addAll(listBD)
    updateViewLocal()
  }

  fun updateViewLocal() {
    val query = subView.getFiltro().query
    val listLocal = list.filter { nota ->
      val cnpj = nota.cnpjEmitente
      val fornecedor = nota.nomeFornecedor
      val chave = nota.chave
      val valorProduto = nota.valorTotalProdutos.format().replace(".", "")
      val valorNota = nota.valorTotal.format().replace(".", "")
      val ni = nota.ni.toString()
      val cfop = nota.cfop.toString()
      query == "" || cnpj == query || fornecedor.contains(query, ignoreCase = true) ||
      chave.contains(query, ignoreCase = true) || valorProduto.startsWith(query) ||
      valorNota.startsWith(query) || ni == query || cfop == query
    }

    subView.updateList(listLocal)
  }

  fun zipXml(notas: List<NotaEntradaXML>): ByteArray {
    return if (notas.isEmpty()) {
      fail("Não ha nenhum item selecionado")
      ByteArray(0)
    }
    else {
      val baos = ByteArrayOutputStream()
      try {
        ZipOutputStream(baos).use { zos ->
          notas.forEach { nota ->
            val xml = nota.xmlFile()
            if (xml != null) {
              val entry = ZipEntry("${nota.chave}.xml")
              zos.putNextEntry(entry)
              zos.write(xml.toByteArray())
            }
          }
          zos.closeEntry()
        }
      } catch (ioe: IOException) {
        ioe.printStackTrace()
      }
      baos.toByteArray()
    }
  }

  fun zipPdf(notas: List<NotaEntradaXML>): ByteArray {
    return if (notas.isEmpty()) {
      fail("Não ha nenhum item selecionado")
      ByteArray(0)
    }
    else {
      val baos = ByteArrayOutputStream()
      try {
        ZipOutputStream(baos).use { zos ->
          notas.forEach { nota ->
            val itensNotaReport = nota.itensNotaReport()
            val report = DanfeReport.create(listOf(itensNotaReport), ETIPO_COPIA.COPIA)
            val entry = ZipEntry("${nota.chave}.pdf")
            zos.putNextEntry(entry)
            zos.write(report)
          }
          zos.closeEntry()
        }
      } catch (ioe: IOException) {
        ioe.printStackTrace()
      }
      baos.toByteArray()
    }
  }
}

interface ITabFileNFEViewModel : ITabView {
  fun getFiltro(): FiltroNotaEntradaXML
  fun updateList(list: List<NotaEntradaXML>)
}