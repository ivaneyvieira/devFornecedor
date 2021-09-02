package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class FornecedorDesconto(
  val vendno: Int,
  val custno: Int,
  val fornecedor: String,
  val notas: List<NotaEntradaDesconto>,
                        ) {
  val labelTitle: String
    get() = "Fornecedor: $vendno - $fornecedor"
  val ultimaData
    get() = notas.map { nota ->
      nota.dataNota
    }.minOfOrNull { it }

  val primeiraData
    get() = notas.map { nota ->
      nota.dataNota
    }.minOfOrNull { it }

  val valorTotal
    get() = notas.sumOf { it.valor }

  companion object {
    fun findAll(filtro: FiltroFornecedor) = saci.descontoDevolucao(filtro).groupBy { it.vendno }.mapNotNull {ent ->
      val nota = ent.value.firstOrNull() ?: return@mapNotNull null
      FornecedorDesconto(vendno = nota.vendno, custno = nota.custno, fornecedor = nota.fornecedor, notas = ent.value)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as FornecedorDesconto

    if (vendno != other.vendno) return false

    return true
  }

  override fun hashCode(): Int {
    return vendno
  }
}