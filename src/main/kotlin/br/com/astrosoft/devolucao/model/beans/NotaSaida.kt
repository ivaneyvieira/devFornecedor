package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaSaida(
  val loja: Int,
  val pdv: Int,
  val transacao: Int,
  val pedido: Int,
  val dataPedido: LocalDate,
  val nota: String,
  val dataNota: LocalDate,
  val fornecedor: String,
  val vendno: Int,
  var rmk: String
               ) {
  fun listaProdutos() = saci.produtosNotaSaida(this)
  
  fun listRepresentantes() = saci.representante(vendno)
  
  fun save() = saci.saveRmk(this)
  
  companion object {
    fun findNotaDevolucao(dataInicial: LocalDate, dataFinal: LocalDate): List<NotaSaida> {
      UltimasNotas.updateList()
      return saci.notasDevolucao(dataInicial, dataFinal)
    }

    fun findNotaVenda(dataInicial: LocalDate, dataFinal: LocalDate): List<NotaSaida> {
      UltimasNotas.updateList()
      return saci.notasVenda(dataInicial, dataFinal)
    }
  }
}