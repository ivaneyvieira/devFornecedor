package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class FornecedorProduto(val vendno: Int,
                        val custno: Int?,
                        val nomeFornecedor: String,
                        val data: LocalDate?,
                        val invno: Int?,
                        val nota: String?) {
  companion object {
    fun findAll(filtro: String): List<FornecedorProduto> {
      return saci.fornecedorProduto(filtro)
    }
  }
}