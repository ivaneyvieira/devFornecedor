package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class Parcela(
    val loja: Int,
    val ni: Int,
    val nota: String,
    val dtVencimento: LocalDate,
    val valor: Double,
    val observacao: String
)