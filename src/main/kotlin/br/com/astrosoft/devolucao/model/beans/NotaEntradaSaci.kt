package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaEntradaSaci(
  invno: Int,
  storeno: Long,
  nomefornecedor: String,
  codigoFornecedor: Long,
  fornecedorSap: Long,
  email: String,
  obs: String,
  numero: String,
  serie: String,
  dataEmissao: LocalDate,
  ordno: Int,
  chave: String,
                     ){
  companion object{
    fun findAll(filtro: FiltroEntradaSaci)= saci.notasEntradaSaci(filtro)
  }
}

data class FiltroEntradaSaci(
  val query: String,
  val dataInicial: LocalDate,
  val dataFinal: LocalDate,
  val chave: String = "",
                            )