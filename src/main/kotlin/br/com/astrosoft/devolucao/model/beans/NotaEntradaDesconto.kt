package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class NotaEntradaDesconto(
  val loja: Int,
  val ni: Int,
  val pedido: Int,
  val nota: String,
  val dataNota: LocalDate,
  val dataEntrada: LocalDate,
  val vendno: Int,
  val custno: Int,
  val fornecedor: String,
  val valor: Double,
  val desconto: Double,
  val pagamento: Double,
  val obsNota: String,
  val chaveDesconto: String,
                         )