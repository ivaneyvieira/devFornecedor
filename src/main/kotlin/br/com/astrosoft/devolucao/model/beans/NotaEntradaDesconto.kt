package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class NotaEntradaDesconto(
  val loja: Int,
  val ni: Int,
  val pedido: Int,
  val nota: String,
  val dataNota: LocalDate,
  val dataEntrada: LocalDate,
  val vencimento: LocalDate,
  val vendno: Int,
  val custno: Int,
  val fornecedor: String,
  val valor: Double,
  val desconto: Double,
  val pagamento: Double,
  val obsNota: String,
  val chaveDesconto: String,
                         ) {

  val obsFormat1: String
    get() {
      return if (obsNota.length > 40) obsNota.substring(0, 40)
      else obsNota
    }

  val obsFormat2: String
    get() {
      return if (obsNota.length > 40) obsNota.substring(40, obsNota.length)
      else ""
    }
}