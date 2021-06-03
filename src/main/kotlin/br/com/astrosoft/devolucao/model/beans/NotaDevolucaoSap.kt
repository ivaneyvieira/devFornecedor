package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class NotaDevolucaoSap(val codigoFor: Int,
                       val nomeFor: String,
                       val vendno: Int = 0,
                       val custno: Int = 0,
                       val storeno: Int,
                       val numero: String,
                       val nfSaci: String = "",
                       val dataLancamento: LocalDate?,
                       val dataVencimento: LocalDate?,
                       val saldo: Double,
                       val saldoSaci: Double = 0.00) {
  val dataLancamentoStr: String
    get() = dataLancamento.format()
}
