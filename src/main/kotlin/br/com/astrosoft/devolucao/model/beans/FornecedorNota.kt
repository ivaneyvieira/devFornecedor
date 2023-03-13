package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class FornecedorNota(
  val loja: Int,
  val ni: Int,
  val nf: String?,
  val emissao: LocalDate?,
  val entrada: LocalDate?,
  val valorNota: Double?,
  val obs: String?,
  val vencimento: LocalDate?,
  val situacao: String?,
  val obsParcela: String?,
                    ) {
  companion object {
    fun findByFornecedor(filtro: FiltroFornecedorNota): List<FornecedorNota> {
      return saci.fornecedorNotas(filtro)
    }
  }
}

data class FiltroFornecedorNota(val vendno: Int, val loja: Int, val query: String)