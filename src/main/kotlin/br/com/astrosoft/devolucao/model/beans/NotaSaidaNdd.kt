package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ItensNotaReport
import br.com.astrosoft.devolucao.model.ProdutoNotaEntradaVO
import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaSaidaNdd(val loja: Int,
                   val numero: Int,
                   val serie: Int,
                   val nota: String,
                   val codigoCliente: Int,
                   val nomeCliente: String,
                   val pedido: Int,
                   val data: LocalDate,
                   val valor: Double,
                   val chave: String) {

  fun produtosNDD(): ProdutoNotaEntradaVO? {
    return ndd.produtosNotasSaida(loja, numero, serie)
  }

  fun itensNotaReport(): List<ItensNotaReport> {
    return produtosNDD()?.itensNotaReport() ?: emptyList()
  }

  companion object {
    fun findAll(filtro: FiltroNotaSaidaNdd): List<NotaSaidaNdd> {
      return saci.notaSaidaNDD(filtro)
    }
  }
}

class FiltroNotaSaidaNdd(val loja: Int?,
                         val nota: String?,
                         val codigoCliente: Int?,
                         val nomeCliente: String?,
                         val dataI: LocalDate?,
                         val dataF: LocalDate?) {
  val numero
    get() = nota?.split("/")?.getOrNull(0)?.toIntOrNull()
  val serie
    get() = nota?.split("/")?.getOrNull(1)
}