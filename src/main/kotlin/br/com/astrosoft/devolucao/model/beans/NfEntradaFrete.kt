package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NfEntradaFrete(
  val loja: Int,
  val ni: String,
  val nf: String,
  val emissao: LocalDate,
  val entrada: LocalDate,
  val vendno: Int,
  val totalPrd: Double,
  val valorNF: Double,
  val carrno: Int,
  val carrName: String,
  val cte: Int,
  val status: String,
  val emissaoCte: LocalDate?,
  val entradaCte: LocalDate?,
  val valorCte: Double?,
  val pesoBruto: Int?,
  val cub: Double?,
  val pesoCub: Double?,
  val fPeso: Double?,
  val adValore: Double?,
  val gris: Double?,
  val taxa: Double?,
  val outro: Double?,
  val aliquota: Double?,
  val icms: Double?,
  val totalFrete: Double?,
                    ) {
  companion object {
    fun findNotas(filter : FiltroDialog): List<NfEntradaFrete> {
      return saci.findNotasEntradaCte(filter)
    }
  }
}

enum class EStatusFrete(val cod: String, val descricao: String) {
  ABERTO("A", "Em Aberto"), PAGO("P", "Pago"), TODOS("T", "Todos"),
}

data class FiltroNFEntradaFrete(
  val loja: Int,
  val di: LocalDate,
  val df: LocalDate,
  val vend: Int,
  val ni: Int,
  val nfno: String,
  val carrno: Int,
  val niCte: Int,
  val cte: Int,
  val tabno: Int,
                               )

data class FiltroDialog(val status: EStatusFrete)