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
                    ) {
  companion object {
    fun findByFornecedor(vendno: Int): List<FornecedorNota> {
      return saci.fornecedorNotas(vendno)
    }
  }
}