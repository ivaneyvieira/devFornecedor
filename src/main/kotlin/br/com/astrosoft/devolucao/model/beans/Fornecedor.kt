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
    get() = notas.maxOf { nota ->
      if (nota.tipo == "PED") nota.dataPedido else nota.dataNota
    }

  val primeiraData
    get() = notas.minOf { nota ->
      if (nota.tipo == "PED") nota.dataPedido else nota.dataNota
    }

  val tipoPag: String
    get() = notas.firstOrNull()?.tipoPag ?: ""

  val documentoPag: String
    get() = notas.firstOrNull()?.documentoPag ?: ""

  val niPag: String
    get() = notas.firstOrNull()?.niPag ?: ""

  val vencimentoPag: String
    get() = notas.firstOrNull()?.vencimentoPag ?: ""

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