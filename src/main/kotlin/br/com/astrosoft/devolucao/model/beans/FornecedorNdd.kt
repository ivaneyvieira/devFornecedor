package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format

data class FornecedorNdd(val cnpj: String, val notas: List<NotaEntradaNdd>) {

  private val fornecedor: FornecedorSaci? by lazy {
    saci.findFornecedor(cnpj)
  }

  val labelTitle: String
  get() = "FORNECEDOR: ${this.vendno} ${this.nome}"


  val custno
    get() = fornecedor?.custno ?: 0
  val nome
    get() = fornecedor?.fornecedor ?: ""
  val vendno
    get() = fornecedor?.vendno ?: 0
  val fornecedorSap
    get() = fornecedor?.fornecedorSap ?: 0
  val email
    get() = fornecedor?.email ?: ""
  val obs
    get() = fornecedor?.obs ?: ""

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
    fun listFornecedores(filtro: String): List<FornecedorNdd> =
            ndd.notasEntrada().groupBy { it.cnpjEmitente }.map { entry ->
              FornecedorNdd(entry.key, entry.value)
            }.filter { it.nome.contains(filtro) || it.vendno == filtro.toIntOrNull() }
  }
}