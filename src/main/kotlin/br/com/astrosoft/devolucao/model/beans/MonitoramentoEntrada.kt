package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class MonitoramentoEntrada {
  var ni: Int = 0
  var loja: Int = 0
  var prdno: String = ""
  var codigo: String = ""
  var grade: String = ""
  var dataEntrada: LocalDate? = null
  var vendno: Int = 0
  var fornecedor: String = ""
  var descricao: String = ""
  var observacao: String = ""

  companion object {
    fun findMonitoramento(filtro: FiltroMonitoramentoEntrada): List<MonitoramentoEntrada> {
      return saci.findMonitoramentoEntrada(filtro)
    }
  }
}

data class FiltroMonitoramentoEntrada(
  val pesquisa: String,
  val loja: Int,
  val dataInicial: LocalDate?,
  val dataFinal: LocalDate?,
)