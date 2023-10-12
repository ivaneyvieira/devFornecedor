package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.viewmodel.entrada.ETemIPI
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.util.format
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.concurrent.thread

data class FornecedorNdd(
  val cnpj: String,
  val custno: Int,
  val nome: String,
  val vendno: Int,
  val fornecedorSap: Int,
  val email: String,
  val obs: String,
  val notas: List<NotaEntradaNdd>
) {
  val temIPI: Boolean by lazy {
    notas.any { it.temIPI }
  }

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

    fun findNota(chave: String): NotaEntradaNdd? {
      return saci
        .notasEntrada(
          FiltroEntradaNdd(
            query = "",
            tipo = ETipoNota.TODOS,
            dataInicial = LocalDate.of(2000, 1, 1),
            dataFinal = LocalDate.now(),
            chave = chave,
          )
        )
        .firstOrNull()
    }

    fun listFornecedores(filtro: FiltroEntradaNdd): List<FornecedorNdd> {
      updateNotas()
      val notaFilter = saci.notasEntrada(filtro).filter {
        when (filtro.temIPI) {
          ETemIPI.TODOS -> true
          ETemIPI.SIM   -> it.temIPI
          ETemIPI.NAO   -> !it.temIPI
        }
      }
      return notaFilter.groupBy { it.cnpjEmitente }.mapNotNull { entry ->
        val notas = entry.value
        if (notas.isEmpty()) null
        else {
          val nota = notas.firstOrNull() ?: return@mapNotNull null
          FornecedorNdd(
            cnpj = nota.cnpjEmitente,
            custno = nota.custno,
            nome = nota.nome,
            vendno = nota.codigoSaci,
            fornecedorSap = nota.fornecedorSap,
            email = nota.email,
            obs = nota.obs,
            notas = notas
          )
        }
      }
    }

    fun updateNotas() {
      val agora = LocalDateTime.now()
      if (agora > datahoraUpdate.plusMinutes(5) && !DB.test) {
        val dataInicial = saci.dataInicialNdd()
        thread(start = true) {
          ndd.notasEntrada(dataInicial) { notas ->
            saci.saveNotaNdd(notas)
          }
          datahoraUpdate = agora
        }
      }
    }
  }
}

data class FiltroEntradaNdd(
  val query: String,
  val tipo: ETipoNota,
  val dataInicial: LocalDate,
  val dataFinal: LocalDate,
  val chave: String = "",
  val temIPI: ETemIPI = ETemIPI.TODOS
)

enum class ETipoNota {
  RECEBER, RECEBIDO, TODOS
}
