package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.CteXML
import br.com.astrosoft.devolucao.model.beans.NotaEntradaFileXML
import br.com.astrosoft.devolucao.model.nfeXml.ProdutoNotaEntradaVO
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.QueryDB
import org.sql2o.ResultSetIterable
import java.time.LocalDate

class QueryNDD : QueryDB(driver, url, username, password) {
  fun notasEntrada(process: (bean: List<NotaEntradaVO>) -> Unit,) {
    val sql = "/sqlNDD/notasEntrada.sql"
    queryLazy(sql, NotaEntradaVO::class, process) {
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

  fun listNFEntrada(chave: String): NotaEntradaFileXML? {
    val sql = "/sqlNDD/listNFEntrada.sql"
    return query(sql, NotaEntradaFileXML::class) {
      addOptionalParameter("chave", "NFe$chave")
    }.firstOrNull()
  }

  fun listCte(cte: Int): CteXML? {
    val sql = "/sqlNDD/listCte.sql"
    return query(sql, CteXML::class) {
      addOptionalParameter("cte", cte)
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

