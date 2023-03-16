package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class TabFileNFEViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabFileNFEViewModel




  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroNotaEntradaFileXML): List<NotaEntradaFileXML> {
    return NotaEntradaFileXML.findAll(filtro)
  }

  fun updateView() {
    val filter = subView.getFiltro()
    val list = NotaEntradaFileXML.findAll(filter)
    subView.updateList(list)
  }

  fun zipXml(notas: List<NotaEntradaFileXML>): ByteArray {
    return if(notas.isEmpty()){
      fail("Não ha nenhum item selecionado")
      ByteArray(0)
    }else{
      val baos = ByteArrayOutputStream()
      try {
        ZipOutputStream(baos).use { zos ->
          notas.forEach {nota->
            val xml = nota.xmlFile
            if(xml != null) {
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
}

interface ITabFileNFEViewModel : ITabView {
  fun getFiltro(): FiltroNotaEntradaFileXML
  fun updateList(list: List<NotaEntradaFileXML>)
}