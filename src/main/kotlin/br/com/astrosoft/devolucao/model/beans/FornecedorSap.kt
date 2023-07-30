package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.planilhas.PlanilhaDevolucaoSap
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.framework.util.format

data class FornecedorSap(
  val codigo: Int,
  val vendno: Int = 0,
  val custno: Int = 0,
  val nome: String,
  val quantidadeNotas: Int,
  val notas: List<NotaDevolucaoSap>
) {
  private val notasSaci = calculaNotasSaci()

  fun notasSaci() = notasSaci.toList()

  private fun calculaNotasSaci(): List<NotaSaida> {
    return saci.notasDevolucao(Serie.Serie01, vendno).filter { nota ->
      nota.serie01Pago == "N"
    }
  }

  val primeiroData get() = notas.mapNotNull { it.dataLancamento }.minByOrNull { it }
  val ultimaData get() = notas.mapNotNull { it.dataLancamento }.maxByOrNull { it }
  val saldoTotal get() = notas.map { it.saldo }.sumOf { it }
  val primeiraDataStr get() = primeiroData.format()
  val ultimaDataStr get() = ultimaData.format()

  val totalSap get() = notas.map { it.saldo }.sumOf { it }
  val totalSaci get() = notasSaci.map { it.valorNota }.sumOf { it }
  val diferenca get() = totalSap - totalSaci

  val labelTitle
    get() = "DEV FORNECEDOR: ${this.custno} ${this.nome} (${this.vendno}) FOR SAP ${this.codigo}"

  companion object {
    fun loadSheet(filename: String) {
      val planilhaDevolucaoSap = PlanilhaDevolucaoSap(filename)
      val fornecedores = planilhaDevolucaoSap.read()
      saci.deleteFornecedorSap()
      fornecedores.forEach { fornecedor ->
        saci.saveFornecedorSap(fornecedor)
      }
    }

    fun findFornecedores(filtro: String): List<FornecedorSap> {
      val listNotaSap = saci.listNotaSap(filtro)
      val fornecedores = listNotaSap.groupBy { it.codigoFor }.mapNotNull { ent ->
        val nota = ent.value.firstOrNull() ?: return@mapNotNull null
        FornecedorSap(
          codigo = nota.codigoFor,
          vendno = nota.vendno,
          custno = nota.custno,
          nome = nota.nomeFor,
          quantidadeNotas = ent.value.size,
          notas = ent.value
        )
      }
      return fornecedores
    }
  }
}
