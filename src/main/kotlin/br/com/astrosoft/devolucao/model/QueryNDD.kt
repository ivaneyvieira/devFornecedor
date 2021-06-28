package br.com.astrosoft.devolucao.model

import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.QueryDB
import java.time.LocalDate

class QueryNDD : QueryDB(driver, url, username, password) {
  fun notasEntrada(): List<NotaEntradaVO> {
    val sql = "/sqlNDD/notasEntrada.sql"
    return query(sql, NotaEntradaVO::class) {
      addOptionalParameter("dataInicial", LocalDate.now().minusMonths(2))
    }
  }

  fun produtosNotasEntrada(id: Int): ProdutoNotaEntradaVO? {
    val sql = "/sqlNDD/notasEntrada.sql"
    return query(sql, ProdutoNotaEntradaVO::class) {
      addOptionalParameter("id", id)
    }.firstOrNull()
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

