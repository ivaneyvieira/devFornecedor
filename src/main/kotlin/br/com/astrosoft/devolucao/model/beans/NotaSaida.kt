package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaSaida(
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
  val email: String,
  val vendno: Int,
  var rmk: String,
  val valor: Double,
  val obsNota: String,
  val serie01Rejeitada: String,
  val serie01Pago: String,
  val remarks: String,
  val baseIcms: Double = 0.00,
  val valorIcms: Double = 0.00,
  val baseIcmsSubst: Double = 0.00,
  val icmsSubst: Double = 0.00,
  val valorFrete: Double = 0.00,
  val valorSeguro: Double = 0.00,
  val valorDesconto: Double = 0.00,
  val outrasDespesas: Double = 0.00,
  val valorIpi: Double = 0.00,
  val valorTotal: Double = 0.00,
  val obsPedido: String,
  val tipo: String
               ) {
  fun listaProdutos() = if(tipo == "PED") saci.produtosPedido(this)
  else saci.produtosNotaSaida(this)
  
  val valorTotalProduto: Double
    get() = listaProdutos().sumByDouble {
      it.valorTotal
    }
  
  fun saveRmk() = saci.saveRmk(this)
  
  fun listFiles() = saci.selectFile(this)
  
  fun chaveFornecedor() = ChaveFornecedor(custno, fornecedor, vendno, email)
  
  companion object {
    private val fornecedores = mutableListOf<Fornecedor>()
    
    fun updateNotasDevolucao(serie: String) {
      val notas = if(serie == "PED") saci.pedidosDevolucao() else saci.notasDevolucao(serie)
      val grupos = notas.groupBy {it.chaveFornecedor()}
      fornecedores.clear()
      fornecedores.addAll(grupos.map {entry ->
        Fornecedor(entry.key.custno, entry.key.fornecedor, entry.key.vendno, entry.key.email, entry.value)
      })
    }
    
    fun findFornecedores() = fornecedores.toList()
  }
}

data class ChaveFornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val email: String
                          )