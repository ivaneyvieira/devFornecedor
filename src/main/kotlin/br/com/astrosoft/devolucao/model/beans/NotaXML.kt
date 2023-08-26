package br.com.astrosoft.devolucao.model.beans

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
)