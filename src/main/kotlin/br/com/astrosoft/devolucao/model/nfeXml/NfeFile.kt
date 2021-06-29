package br.com.astrosoft.devolucao.model.nfeXml

import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota
import com.fincatto.documentofiscal.utils.DFPersister

class NfeFile(xmlContent: String) {

  private val nota: NFNota = DFPersister(false).read(NFNota::class.java, xmlContent)

  fun print() {
    print(nota)
  }
}/*
fun main() {
  val file = File("nfe/nfe.xml")
  val text = file.readText()
  print(text)
  val nota = NfeFile(text)
  nota.print()
}
 */