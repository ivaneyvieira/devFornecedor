package br.com.astrosoft.devolucao.model.knfe

fun main() {
  val file = NFe::class.java.getResourceAsStream("/nfe/nota.xml")
  val xml = file?.readAllBytes()?.decodeToString() ?: ""
  val timei = System.currentTimeMillis()
  val nfe = parseNotaFiscal(xml)
  val timef = System.currentTimeMillis()
  println("Tempo: ${timef - timei}")
  println(nfe)
}