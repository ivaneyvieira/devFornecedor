package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaFileXML
import br.com.astrosoft.devolucao.model.beans.NotaEntradaFileXML
import br.com.astrosoft.devolucao.model.nfeXml.ProdutoNotaEntradaVO
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.toSaciDate
import java.time.LocalDate

class QueryNDD : QueryDB(driver, url, username, password) {
  fun notasEntrada(): List<NotaEntradaVO> {
    val sql = "/sqlNDD/notasEntrada.sql"
    return query(sql, NotaEntradaVO::class) {
      addOptionalParameter("dataInicial", LocalDate.now().minusMonths(7))
    }
  }

  fun produtosNotasEntrada(id: Int): ProdutoNotaEntradaVO? {
    val sql = "/sqlNDD/produtosNotaEntrada.sql"
    return query(sql, ProdutoNotaEntradaVO::class) {
      addOptionalParameter("id", id)
    }.firstOrNull()
  }

  fun produtosNotasSaida(storeno: Int, numero: Int, serie: Int): ProdutoNotaEntradaVO? {
    val sql = "/sqlNDD/produtosNotaSaida.sql"
    return query(sql, ProdutoNotaEntradaVO::class) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("numero", numero)
      addOptionalParameter("serie", serie)
    }.firstOrNull()
  }

  fun listNFENtrada(filter: FiltroNotaEntradaFileXML): List<NotaEntradaFileXML>{
    val sql = "/sqlNDD/listNFEntrada.sql"
    return query(sql, NotaEntradaFileXML::class) {
      addOptionalParameter("dataInicial", "${filter.dataInicial.toSaciDate()}")
      addOptionalParameter("dataFinal", "${filter.dataFinal.toSaciDate()}")
      addOptionalParameter("numero", filter.numero)
      addOptionalParameter("cnpj", filter.cnpj)
      addOptionalParameter("cnpjLoja", filter.loja?.cnpjLoja ?: "")
      addOptionalParameter("fornecedor", filter.fornecedor)
    }
  }

  companion object {
    private val db = DB("ndd")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
  }
}

val ndd = QueryNDD()

