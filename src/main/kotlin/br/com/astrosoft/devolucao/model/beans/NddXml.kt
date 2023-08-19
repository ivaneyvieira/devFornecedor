package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.knfe.Detalhe
import br.com.astrosoft.devolucao.model.knfe.parseNotaFiscal
import br.com.astrosoft.devolucao.model.saci

class NddXml(val id: Int, val xml: String) {
  companion object {
    private val mapNddXml = mutableMapOf<NiProd, List<Detalhe>>()
    fun detalheProduto(
      ni: Int,
      loja: Int,
      numero: String,
      serie: String,
      cProd: String,
      listBarcode: List<String>
    ): List<Detalhe> {
      return listBarcode.flatMap { barcode ->
        detalheProduto(ni, loja, numero, serie, cProd, barcode)
      }
    }

    private val regexGtin = Regex("""\d{8}|\d{12,14}""")

    private fun detalheProduto(
      ni: Int,
      loja: Int,
      numero: String,
      serie: String,
      cProd: String,
      barcode: String
    ): List<Detalhe> {
      return mapNddXml.getOrPut(NiProd(ni, cProd, barcode)) {
        val xml = saci.findXmlNfe(ni, loja, numero, serie)?.xml ?: return@getOrPut emptyList()
        val nfe = parseNotaFiscal(xml)
        val gtinValido = regexGtin.matches(barcode)
        nfe?.infNFe?.detalhes?.filter { det ->
          det.prod?.cProd == cProd || (gtinValido && (det.prod?.cEAN == barcode || det.prod?.cEANTrib == barcode))
        }.orEmpty()
      }
    }
  }
}

data class NiProd(val ni: Int, val prod: String, val barcode: String)