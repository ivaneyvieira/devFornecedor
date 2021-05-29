package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class NotaDevolucaoSap(val codigoFor: Int,
                       val nomeFor: String,
                       val vendno: Int = 0,
                       val storeno: Int,
                       val numero: String,
                       val dataLancamento: LocalDate?,
                       val dataVencimento: LocalDate?,
                       val saldo: Double,
                       val saldoSaci: Double = 0.00)
