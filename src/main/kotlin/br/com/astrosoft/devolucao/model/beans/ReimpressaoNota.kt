package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class ReimpressaoNota(
  val data: LocalDate,
  val hora: String,
  val loja: Int,
  val nota: String,
  val tipo: String,
  val usuario: String,
  val dataNota: LocalDate?,
  val codcli: Int?,
  val nomecli: String?,
  val valor: Double?,
) {
  companion object {
    fun findReimpressao(filtro: FiltroReimpressao): List<ReimpressaoNota> = saci.findReimpressao(filtro)
  }
}

data class FiltroReimpressao(val loja: Int, val filtro: String)