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
  val fatura: String,
  val dataNota: LocalDate,
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  var rmk: String,
  val valor: Double,
  val obsNota: String
               ) {
  fun listaProdutos() = saci.produtosNotaSaida(this)
  
  fun save() = saci.saveRmk(this)
  
  fun listFiles() = saci.selectFile(this)
  
  fun chaveFornecedor() = ChaveFornecedor(custno, fornecedor, vendno)
  
  fun serieNota() = nota.split("/").getOrNull(1) ?: ""
  
  companion object {
    private val fornecedores = mutableListOf<Fornecedor>()
    
    fun updateNotasDevolucao(serie: String) {
      val notas = saci.notasDevolucao(serie)
      val grupos = notas.groupBy {it.chaveFornecedor()}
      fornecedores.clear()
      fornecedores.addAll(grupos.map {entry ->
        Fornecedor(entry.key.custno, entry.key.fornecedor, entry.key.vendno, entry.value)
      })
    }
    
    fun findFornecedores() = fornecedores.toList()
  }
}

data class ChaveFornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int
                          )