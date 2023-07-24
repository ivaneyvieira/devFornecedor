package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.NotaEntradaVO
import br.com.astrosoft.devolucao.model.knfe.parseNotaFiscal

class ProdutosNdd(
  val id: Int,
  val nItem: Int,
  val cProd: String,
  val cEAN: String,
  val cEANTrib: String,
  val xProd: String,
  val ncm: String,
  val cfop: String,
  val uCom: String,
  val qCom: Double,
  val vUnCom: Double,
  val vProd: Double,
  val indTot: String,
  val cstIcms: String,
  val baseIcms: Double,
  val percIcms: Double,
  val valorIcms: Double,
  val baseIpi: Double,
  val percIpi: Double,
  val valorIpi: Double,
  val basePis: Double,
  val percPis: Double,
  val valorPis: Double,
  val baseCofins: Double,
  val percCofins: Double,
  val valorCofins: Double,
) {
  companion object {
    fun fromNdd(nota: NotaEntradaVO): List<ProdutosNdd> {
      val xml = nota.xmlNfe
      return fromXml(nota.id, xml)
    }

    fun fromNdd(nota: NotaEntradaNdd): List<ProdutosNdd> {
      val xml = nota.xmlNfe
      return fromXml(nota.id, xml)
    }

    private fun fromXml(
      id: Int,
      xml: String,
    ): List<ProdutosNdd> {
      val detalhes = parseNotaFiscal(xml)?.infNFe?.detalhes.orEmpty()
      return detalhes.mapNotNull { det ->
        val imposto = det.imposto ?: return@mapNotNull null
        val produto = det.prod ?: return@mapNotNull null
        ProdutosNdd(
          id = id,
          nItem = det.nItem,
          cProd = produto.cProd,
          cEAN = produto.cEAN,
          cEANTrib = produto.cEANTrib,
          xProd = produto.xProd,
          ncm = produto.ncm,
          cfop = produto.cfop,
          uCom = produto.uCom,
          qCom = produto.qCom,
          vUnCom = produto.vUnCom,
          vProd = produto.vProd,
          indTot = produto.indTot,
          cstIcms = imposto.icms?.let { "${it.orig}${it.cst}" } ?: "",
          baseIcms = imposto.icms?.vBC ?: 0.00,
          percIcms = imposto.icms?.pICMS ?: 0.00,
          valorIcms = imposto.icms?.vICMS ?: 0.00,
          baseIpi = imposto.ipi?.vBC ?: 0.00,
          percIpi = imposto.ipi?.pIPI ?: 0.00,
          valorIpi = imposto.ipi?.vIPI ?: 0.00,
          basePis = imposto.pis?.vBC ?: 0.00,
          percPis = imposto.pis?.pPIS ?: 0.00,
          valorPis = imposto.pis?.vPIS ?: 0.00,
          baseCofins = imposto.cofins?.vBC ?: 0.00,
          percCofins = imposto.cofins?.pCOFINS ?: 0.00,
          valorCofins = imposto.cofins?.vCOFINS ?: 0.00,
        )
      }
    }
  }
}
