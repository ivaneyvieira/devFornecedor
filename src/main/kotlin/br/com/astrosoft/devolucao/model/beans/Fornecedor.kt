package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format

class Fornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val fornecedorSap: Int,
  val email: String,
  val tipo: String,
  var obs: String,
  val notas: List<NotaSaida>,
                ) {
  fun listRepresentantes() = saci.representante(vendno)

  fun parcelasFornecedor() = saci.listParcelasFornecedor(vendno)

  fun pedidosFornecedor() = saci.listPedidosFornecedor(vendno)

  val ultimaData
    get() = notas.mapNotNull { nota ->
      if (nota.tipo == "PED") nota.dataPedido else nota.dataNota
    }.minOfOrNull { it }

  val primeiraData
    get() = notas.mapNotNull { nota ->
      if (nota.tipo == "PED") nota.dataPedido else nota.dataNota
    }.minOfOrNull { it }

  private val notaObs
    get() = notas.sortedBy { it.dataNota }.lastOrNull { !it.chaveDesconto.isNullOrBlank() }

  val chaveDesconto: String
    get() = notaObs?.chaveDesconto ?: ""

  val tipoPag: String
    get() = notaObs?.tipoPag ?: ""

  val documentoPag: String
    get() = notaObs?.documentoPag ?: ""

  val niPag: String
    get() = notaObs?.niPag ?: ""

  val vencimentoPag: String
    get() = notaObs?.vencimentoPag ?: ""

  val ultimaDataStr
    get() = ultimaData.format()

  val primeiraDataStr
    get() = primeiraData.format()

  val valorTotal
    get() = notas.sumOf { it.valor }

  fun listEmail(): List<String> {
    val list = listRepresentantes().map {
      it.email
    } + email

    return list.distinct().filter {
      it != ""
    }.sorted()
  }

  fun saveRmkVend() {
    saci.saveRmkVend(this)
  }

  val labelTitle
    get() = "DEV FORNECEDOR: ${this.custno} ${this.fornecedor} (${this.vendno}) FOR SAP ${this.fornecedorSap}"
}

data class FiltroFornecedor(val txt: String, val loja: Loja = Loja.lojaZero)