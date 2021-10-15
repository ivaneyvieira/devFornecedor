package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

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

  val dataAgenda
    get() = notaObs?.dataAgenda

  val notaObs
    get() = notas.sortedBy { it.dataNota }
      .firstOrNull()

  val chaveDesconto: String
    get() {
      val nota = notaObs ?: return ""
      return nota.chaveDesconto ?: ""
    }

  val ultimaDataStr
    get() = ultimaData.format()

  val primeiraDataStr
    get() = primeiraData.format()

  val valorTotal
    get() = notas.sumOf { it.valor }

  /********** Campos Situação ************/

  val dataSituacao: LocalDate?
    get() = notaObs?.dataSituacao
  val situacao: String
    get() = notaObs?.situacao ?: ""
  val situacaoStr: String
    get() = notaObs?.situacaoStr ?: ""
  val usuarioSituacao: String
    get() = notaObs?.usuarioSituacao ?: ""
  val notaSituacao: String
    get() = notaObs?.notaSituacao ?: ""
  val tituloSituacao: String
    get() = notaObs?.tituloSituacao ?: ""
  val docSituacao: String
    get() = notaObs?.docSituacao ?: ""
  val niSituacao: String
    get() = notaObs?.niSituacao ?: ""

  /********************************************/

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

  fun notasNaoRecebidasFornecedor(): List<NotaEntradaNdd> {
    val filtro =
            FiltroEntradaNdd(query = vendno.toString(),
                             tipo = ETipoNota.RECEBER,
                             dataInicial = LocalDate.of(2021, 1, 1),
                             dataFinal = LocalDate.now())
    return saci.notasEntrada(filtro)
  }

  val labelTitle
    get() = "DEV FORNECEDOR: ${this.custno} ${this.fornecedor} (${this.vendno}) FOR SAP ${this.fornecedorSap}"
}

data class FiltroFornecedor(val query: String, val loja: Loja = Loja.lojaZero)