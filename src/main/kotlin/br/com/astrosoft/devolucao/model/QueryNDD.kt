package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.framework.model.Config.appName
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.toSaciDate

class QueryNDD : QueryDB(driver, url, username, password) {
  fun notasEntrada(): List<NotaEntradaVO> {
    val sql = "/sqlNDD/notasEntrada.sql"
    return query(sql, NotaEntradaVO::class)
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

