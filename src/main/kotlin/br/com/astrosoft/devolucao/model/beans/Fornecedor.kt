package br.com.astrosoft.devolucao.model.beans

class Fornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val notas: List<NotaSaida>
)