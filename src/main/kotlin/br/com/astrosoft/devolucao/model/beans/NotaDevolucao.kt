package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaDevolucao(
  val loja: Int,
  val pdv: Int,
  val transacao: Int,
  val pedido: Int,
  val dataPedido: LocalDate,
  val nota: String,
  val dataNota: LocalDate,
  val fornecedor: String,
  val vendno: Int
                   ) {
  fun listaProdutos() = saci.produtosNotaSaida(this)
  
  fun listRepresentantes() = saci.representante(vendno)
  
  companion object {
    fun all() = saci.notasDevolucao()
  }
}