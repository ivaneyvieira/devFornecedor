package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.nfeXml.ItensNotaReport
import br.com.astrosoft.devolucao.model.nfeXml.ProdutoNotaEntradaVO
import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.Config
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

  fun reimpresao(): ReimpressaoNota? {
    val login = Config.user?.login ?: return null
    return saci.findReimpressao(loja, nota, login).firstOrNull()
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

  fun isEmpty() =
    (nota == null || nota == "") && (codigoCliente == null || codigoCliente == 0) && (nomeCliente == null || nomeCliente == "") && (dataI == null) && (dataF == null)
}