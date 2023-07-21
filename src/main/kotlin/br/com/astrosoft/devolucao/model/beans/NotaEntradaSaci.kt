package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaEntradaSaci(
  val invno: Int,
  val storeno: Long,
  val nomefornecedor: String,
  val codigoFornecedor: Long,
  val fornecedorSap: Long,
  val email: String,
  val obs: String,
  val numero: String,
  val serie: String,
  val dataEmissao: LocalDate,
  val ordno: Int,
  val chave: String,
) {
  companion object {
    fun findAll(filtro: FiltroEntradaSaci) = saci.notasEntradaSaci(filtro)
  }
}

data class FiltroEntradaSaci(
  val query: String,
  val dataInicial: LocalDate,
  val dataFinal: LocalDate,
  val chave: String = "",
)