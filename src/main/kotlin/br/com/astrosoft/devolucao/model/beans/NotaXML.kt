package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.beans.EDiferencaNum.*
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

data class NotaXML(
  val lj: Int,
  val ni: Int,
  val data: LocalDate?,
  val dataEmissao: LocalDate?,
  val nfe: String,
  val serie: String,
  val fornCad: String,
  val fornNota: String,
  val codigo: String?,
  val refPrdx: String?,
  val descricaox: String?,
  val cstx: String?,
  val mvax: Double,
  val barcodex: String?,
  val cfopx: String?,
  val alIcmsx: Double,
  val alPisx: Double,
  val alCofinsx: Double,
  val unidadex: String?,
  val alIpix: Double,
  val quant: Double,
  val quantSaci: Int,
  val unidadeSaci: String,
) {
  override fun toString(): String {
    return "$lj|$ni|${data.format()}|${dataEmissao.format()}|$nfe|$serie|$fornNota|$codigo|$refPrdx|$descricaox|$cstx|${mvax.format()}|$barcodex|$cfopx|${alIcmsx.format()}|${alPisx.format()}|${alCofinsx.format()}|$unidadex|${alIpix.format()}|${quant.format()}|${quantSaci.format()}|$unidadeSaci"
  }

  fun quantDiferenca(): EDiferencaNum {
    return when {
      quant > quantSaci.toDouble()  -> DP
      quant < quantSaci.toDouble()  -> DN
      quant == quantSaci.toDouble() -> S
      else                          -> T
    }
  }
}