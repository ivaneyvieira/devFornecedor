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
    private val fornecedores = mutableListOf<FornecedorDesconto>()
    fun findAll(filtro: FiltroFornecedor): List<FornecedorDesconto> {
      if (filtro.query == "") {
        val listFor = saci.descontoDevolucao(filtro).groupBy { it.vendno }.mapNotNull { ent ->
          val nota = ent.value.firstOrNull() ?: return@mapNotNull null
          FornecedorDesconto(
              vendno = nota.vendno,
              custno = nota.custno,
              fornecedor = nota.fornecedor,
              notas = ent.value
          )
        }
        fornecedores.clear()
        fornecedores.addAll(listFor)
        return fornecedores
      } else {
        return fornecedores.filter { fornecedor ->
          fornecedor.filtroByNoLoja(filtro.loja.no) && fornecedor.filtroByQuery(filtro.query)
        }
      }
    }
  }

  private fun filtroByQuery(query: String): Boolean {
    val queryNo = query.trim().toIntOrNull()
    return if (queryNo == null) this.fornecedor.contains(query.trim(), ignoreCase = true)
    else this.fornecedor.contains(query.trim(), ignoreCase = true) || this.custno == queryNo || this.vendno == queryNo
  }

  private fun filtroByNoLoja(no: Int): Boolean {
    return if (no == 0) true else this.notas.any { it.loja == no }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as FornecedorDesconto

    return vendno == other.vendno
  }

  override fun hashCode(): Int {
    return vendno
  }
}