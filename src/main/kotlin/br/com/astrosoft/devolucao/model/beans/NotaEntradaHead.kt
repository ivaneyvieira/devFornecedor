package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

open class NotaEntradaHead(
  val lj: Int,
  val ni: Int,
  val data: LocalDate?,
  val dataEmissao: LocalDate?,
  val nfe: String,
  val fornCad: String,
  val fornNota: String,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as NotaEntradaHead

    return ni == other.ni
  }

  override fun hashCode(): Int {
    return ni
  }
}

class NotaEntradaHeadList(
  lj: Int,
  ni: Int,
  data: LocalDate?,
  dataEmissao: LocalDate?,
  nfe: String,
  fornCad: String,
  fornNota: String,
  val list: List<NfPrecEntrada>,
) : NotaEntradaHead(lj, ni, data, dataEmissao, nfe, fornCad, fornNota)

