package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class MonitoramentoEntrada {
  var ni: Int = 0
  var loja: Int = 0
  var sigla: String = ""
  var prdno: String = ""
  var refFor: String = ""
  var codigo: String = ""
  var grade: String = ""
  var barcode: String = ""
  var dataEntrada: LocalDate? = null
  var nota: String = ""
  var pedido: Int = 0
  var valorNota: Double = 0.0
  var vendno: Int = 0
  var custno: Int = 0
  var fornecedorSap: Int = 0
  var fornecedor: String = ""
  var descricao: String = ""
  var un: String = ""
  var observacao: String = ""
  var valorUnit: Double = 0.0
  var quant: Int = 0
  var valorTotal: Double = 0.0

  var item: Int = 0

  val dataEntradaStr
    get() = dataEntrada.format()

  companion object {
    fun findMonitoramento(filtro: FiltroMonitoramentoEntrada): List<MonitoramentoEntradaFornecedor> {
      return saci.findMonitoramentoEntrada(filtro).groupBy { it.vendno }.mapNotNull { (vendno, list) ->
        val beanForn = list.firstOrNull() ?: return@mapNotNull null
        val fornecedor = beanForn.fornecedor
        val custno = beanForn.custno
        MonitoramentoEntradaFornecedor(
          vendno = vendno,
          custno = custno,
          fornecedor = fornecedor,
          valorTotal = list.groupBy { it.ni }.mapNotNull { it.value.firstOrNull()?.valorNota }.sum(),
          produtos = list
        )
      }
    }
  }
}

data class FiltroMonitoramentoEntrada(
  val pesquisa: String,
  val loja: Int,
  val dataInicial: LocalDate?,
  val dataFinal: LocalDate?,
)

data class MonitoramentoEntradaFornecedor(
  val vendno: Int = 0,
  val custno: Int = 0,
  val fornecedor: String = "",
  val valorTotal: Double,
  val produtos: List<MonitoramentoEntrada>
) {
  val labelTitle: String
    get() = "Fornecedor: $vendno $fornecedor ($custno)"

  val notas
    get() = produtos.groupBy { it.ni }.mapNotNull { (ni, list) ->
      val bean = list.firstOrNull() ?: return@mapNotNull null
      MonitoramentoEntradaNota(
        ni = ni,
        loja = bean.loja,
        sigla = bean.sigla,
        dataEntrada = bean.dataEntrada,
        nota = bean.nota,
        pedido = bean.pedido,
        valorNota = bean.valorNota,
        vendno = bean.vendno,
        custno = bean.custno,
        fornecedorSap = bean.fornecedorSap,
        fornecedor = bean.fornecedor,
        observacao = bean.observacao,
        valorTotal = list.sumOf { it.valorNota },
        produtos = list
      )
    }
}

data class MonitoramentoEntradaNota(
  val ni: Int,
  val loja: Int,
  val sigla: String,
  val dataEntrada: LocalDate?,
  val nota: String,
  val pedido: Int,
  val valorNota: Double,
  val vendno: Int,
  val custno: Int,
  val fornecedorSap: Int,
  val fornecedor: String,
  val observacao: String,
  val valorTotal: Double,
  val produtos: List<MonitoramentoEntrada>
)