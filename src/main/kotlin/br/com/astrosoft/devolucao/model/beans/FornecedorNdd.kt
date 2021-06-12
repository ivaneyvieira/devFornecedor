package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDateTime
import kotlin.concurrent.thread

data class FornecedorNdd(val cnpj: String,
                         val custno: Int,
                         val nome: String,
                         val vendno: Int,
                         val fornecedorSap: Int,
                         val email: String,
                         val obs: String,
                         val notas: List<NotaEntradaNdd>) {
  val labelTitle: String
    get() = "FORNECEDOR: ${this.vendno} ${this.nome}"

  val ultimaData
    get() = notas.mapNotNull { it.dataEmissao }.maxOrNull()

  val primeiraData
    get() = notas.mapNotNull { it.dataEmissao }.minOrNull()

  val ultimaDataStr
    get() = ultimaData.format()

  val primeiraDataStr
    get() = primeiraData.format()

  val valorTotal
    get() = notas.sumOf { it.baseCalculoIcms }

  companion object {
    private var datahoraUpdate = LocalDateTime.now().minusDays(1)

    fun listFornecedores(filtro: FiltroEntradaNdd): List<FornecedorNdd> {
      updateNotas()
      return saci.notasEntrada(filtro).groupBy { it.cnpjEmitente }.mapNotNull { entry ->
        val notas = entry.value
        if (notas.isEmpty()) null
        else {
          val nota = notas.firstOrNull() ?: return@mapNotNull null
          FornecedorNdd(nota.cnpjEmitente,
                        nota.custno,
                        nota.nome,
                        nota.vendno,
                        nota.fornecedorSap,
                        nota.email,
                        nota.obs,
                        notas)
        }
      }
    }

    fun updateNotas() {
      val agora = LocalDateTime.now()
      if (agora > datahoraUpdate.plusMinutes(5)) {
        thread(start = true) {
          val notas = ndd.notasEntrada()
          saci.saveNotaNdd(notas)
          datahoraUpdate = agora
        }
      }
    }
  }
}

data class FiltroEntradaNdd(val query: String, val tipo: ETipoNota)

enum class ETipoNota {
  RECEBER, RECEBIDO, TODOS
}
