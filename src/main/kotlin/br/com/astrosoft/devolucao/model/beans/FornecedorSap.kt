package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.planilhas.PlanilhaDevolucaoSap
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie

data class FornecedorSap(val codigo: Int,
                         val vendno: Int = 0,
                         val custno: Int = 0,
                         val nome: String,
                         val quantidadeNotas: Int,
                         val notas: List<NotaDevolucaoSap>) {
  fun notasSaci(): List<NotaSaida> {
    return saci.notasDevolucao(Serie.Serie01).filter { nota ->
      nota.serie01Pago == "N" && nota.vendno == vendno
    }
  }

  val primeiroData get() = notas.mapNotNull { it.dataLancamento }.minByOrNull { it }
  val ultimaData get() = notas.mapNotNull { it.dataLancamento }.maxByOrNull { it }
  val saldoTotal get() = notas.map { it.saldo }.sumOf { it }

  val labelTitle
    get() = "DEV FORNECEDOR: ${this.custno} ${this.nome} (${this.vendno}) FOR SAP ${this.codigo}"

  companion object {
    fun loadSheet(filename: String) {
      val planilhaDevolucaoSap = PlanilhaDevolucaoSap(filename)
      val fornecedores = planilhaDevolucaoSap.read()
      fornecedores.forEach { fornecedor ->
        saci.saveFornecedorSap(fornecedor)
      }
    }

    fun findFornecedores(filtro: String): List<FornecedorSap> {
      val listNotaSap = saci.listNotaSap(filtro)
      val fornecedores = listNotaSap.groupBy { it.codigoFor }.mapNotNull { ent ->
        val nota = ent.value.firstOrNull() ?: return@mapNotNull null
        FornecedorSap(codigo = nota.codigoFor,
                      vendno = nota.vendno,
                      custno = nota.custno,
                      nome = nota.nomeFor,
                      quantidadeNotas = ent.value.size,
                      notas = ent.value)
      }
      return fornecedores
    }
  }
}
