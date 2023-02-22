package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

data class NfEntradaFrete(
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
  val pesoBruto: Double?,
  val cub: Double?,
  val pesoCub: Double?,
  val fretePeso: Double?,
  val adValore: Double?,
  val gris: Double?,
  val taxa: Double?,
  val outro: Double?,
  val aliquota: Double?,
  val icms: Double?,
  val totalFrete: Double?,
                         ) {

  val dataStr
    get() = entradaCte.format()
  val freteDif
    get() = totalFrete.format() != valorCte.format()

  companion object {
    fun findNotas(filter: FiltroDialog): List<NfEntradaFrete> {
      return saci.findNotasEntradaCte(filter)
    }
  }
}

enum class EStatusFrete(val cod: String, val descricao: String) {
  ABERTO("A", "Aberto"), PAGO("P", "Pago"), TODOS("T", "Todos"),
}

enum class EDifFrete(val cod: String, val descricao: String) {
  IGUAL("=", "Igual"), DIFPos(">", "Diferente >"), DIFNeg("<", "Diferente <"), TODOS("T", "Todos"),
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

data class FiltroDialog(val status: EStatusFrete, val diferenca : EDifFrete)

data class NfFreteGrupo(val nomeGrupo: String,
                        val nota: NfEntradaFrete,
                        val cte: Int,
                        val valorNota: String,
                        val valorCalculado: String) {
  val lj = nota.loja
  val ni = nota.ni
  val dataStr = nota.dataStr
  val nfe = nota.nf
  val fornCad = nota.vendno
}