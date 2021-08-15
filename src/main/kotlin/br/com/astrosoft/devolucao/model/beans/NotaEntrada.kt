package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaEntrada(val nfekey: String,
                  val nfno: Int,
                  val nfse: String,
                  val dataNota: LocalDate?,
                  val horaNota: String,
                  val vendno: Int,
                  val storeno: Int,
                  val fornecedor: String,
                  val loja: String) {
  val nota
    get() = "$nfno/$nfse"

  companion object {
    fun listNotasPendentes() = saci.listaNotasPendentes()
  }
}