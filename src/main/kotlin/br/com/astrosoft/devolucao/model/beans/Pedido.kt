package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class Pedido(
  val loja: Int,
  val sigla: String,
  val pdv: Int,
  val transacao: Int,
  val pedido: Int,
  val dataPedido: LocalDate,
  val nota: String,
  val fatura: String,
  val dataNota: LocalDate,
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val valor: Double
            ) {
  fun listaProdutos(): List<ProdutosPedido> = saci.produtosPedido(loja, pedido)
  
  val valorTotalProduto: Double
    get() = listaProdutos().sumByDouble {
      it.valorTotal
    }
  
  fun chaveFornecedor() = ChaveFornecedor(custno, fornecedor, vendno)
  
  fun serieNota() = nota.split("/")
                      .getOrNull(1) ?: ""
  
  companion object {
    fun findPedidos() = saci.pedidosDevolucao()
  }
}