package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class UltimasNotas(val ni: Int,
                   val loja: Int,
                   val nfNumero: String,
                   val data: LocalDate,
                   val codigo: String,
                   val grade: String,
                   val qttd: Int) {
  companion object {
    private var ultimasNota: Map<ProdutoGrade, List<UltimasNotas>> = emptyMap()

    fun updateList() {
      ultimasNota = saci.ultimasNotas().groupBy {
        ProdutoGrade(it.codigo, it.grade)
      }
    }

    fun ultimasNotas(codigo: String, grade: String): List<UltimasNotas> {
      return ultimasNota[ProdutoGrade(codigo, grade)].orEmpty()
    }
  }
}

data class ProdutoGrade(val codigo: String, val grade: String)