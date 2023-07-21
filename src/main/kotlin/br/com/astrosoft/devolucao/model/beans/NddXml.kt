package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.knfe.Detalhe
import br.com.astrosoft.devolucao.model.knfe.parseNotaFiscal
import br.com.astrosoft.devolucao.model.saci

class NddXml(val id: Int, val xml: String) {
  companion object {
    private val mapNddXml = mutableMapOf<NiProd, Detalhe?>()
    fun detalheProduto(ni: Int, cProd: String, barcode: String): Detalhe? {
      return mapNddXml.getOrPut(NiProd(ni, cProd, barcode)) {
        val xml = saci.findXmlNfe(ni)?.xml ?: return@getOrPut null
        val nfe = parseNotaFiscal(xml)
        nfe.infNFe.detalhes.firstOrNull { det ->
          det.prod?.cProd == cProd || det.prod?.cEAN == barcode
        }
      }
    }
  }
}

data class NiProd(val ni: Int, val prod: String, val barcode: String)