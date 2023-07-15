package br.com.astrosoft.devolucao.model.beans

class FornecedorEntrada(val vendno: Int, val fornecedor: String, val notas: List<NotaEntrada>) {
  val ultimaData
    get() = notas.mapNotNull { it.dataNota }.maxOfOrNull { it }

  companion object {
    fun listFornecedores() =
      NotaEntrada.listNotasPendentes().groupBy { ChaveFornecedorEntrada(it.vendno, it.fornecedor) }.map { group ->
        FornecedorEntrada(group.key.vendno, group.key.fornecedor, group.value)
      }
  }
}

data class ChaveFornecedorEntrada(val vendno: Int, val fornecedor: String)