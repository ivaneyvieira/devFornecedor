package br.com.astrosoft.devolucao.model.beans

import kotlin.math.absoluteValue

data class ProdutoNotaEntradaNdd(val codigo: String,
                                 val codBarra: String,
                                 val descricao: String,
                                 val ncm: String,
                                 val cst: String,
                                 val cfop: String,
                                 val un: String,
                                 val quantidade: Double,
                                 val valorUnitario: Double,
                                 val valorTotal: Double,
                                 val baseICMS: Double,
                                 val valorIPI: Double,
                                 val aliqICMS: Double,
                                 val aliqIPI: Double) {
  val temIPI
    get() = valorIPI.absoluteValue > 0.001
}