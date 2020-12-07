package br.com.astrosoft.devolucao.model.beans

class ProdutosPedido (
  val loja: Int,
  val pedido: Int,
  val codigo: String,
  val descricao: String,
  val grade: String,
  val qtde: Int,
  val barcode: String,
  val un: String,
  val valorUnitario: Double,
  val valorTotal: Double
                     )