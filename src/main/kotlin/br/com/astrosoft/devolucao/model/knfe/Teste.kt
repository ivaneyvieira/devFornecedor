package br.com.astrosoft.devolucao.model.knfe

fun main() {
  val file = NFe::class.java.getResourceAsStream("/nfe/nota.xml")
  val xml = file?.readAllBytes()?.decodeToString() ?: ""
  val timei = System.currentTimeMillis()
  val nfe = parseNotaFiscal(xml)
  val timef = System.currentTimeMillis()
  nfe.infNFe.detalhes.forEach {
    println(it.prod?.xProd)
    println(it.imposto?.icms?.cst)
    println(it.imposto?.icms?.pICMS)
  }
  println("Tempo: ${timef - timei}")
}