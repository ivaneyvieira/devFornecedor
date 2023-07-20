package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.knfe.ICMS
import br.com.astrosoft.devolucao.model.knfe.parseNotaFiscal
import br.com.astrosoft.devolucao.model.saci

class NddXml(val id: Int, val xml: String) {
  companion object {
    private val mapNddXml = mutableMapOf<NiProd, ICMS?>()
    fun cst(ni: Int, cProd: String): String? {
      val icms = mapNddXml.getOrPut(NiProd(ni, cProd)) {
        val xml = saci.findXmlNfe(ni)?.xml ?: return@getOrPut null
        val nfe = parseNotaFiscal(xml)
        val icms = nfe.infNFe.detalhes.firstOrNull { det ->
          det.prod?.cProd == cProd
        }?.imposto?.icms ?: return null

        return@getOrPut icms
      } ?: return null


      val ori = icms.orig
      val cst = icms.cst
      return "$ori$cst"
    }
  }
}

data class NiProd(val ni: Int, val prod: String)