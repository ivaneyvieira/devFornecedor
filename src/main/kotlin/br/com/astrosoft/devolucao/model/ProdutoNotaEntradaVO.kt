package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNddd

class ProdutoNotaEntradaVO(val id: Int, val xmlNfe: String) {
  fun produtosNotaEntradaNDD(): List<ProdutoNotaEntradaNddd> {
    val produtosXML = produtosXML()
    TODO()
  }

  private fun produtosXML(): List<String> {
    val xmlLine = xmlNfe.replace("\n", "")
    val regex = "<det.+</det>".toRegex()
    var matchResult = regex.find(xmlLine)
    return sequence {
      while (matchResult != null) {
        val value = matchResult?.value
        if (value != null) yield(value)
        matchResult = matchResult?.next()
      }
    }.toList()
  }
}