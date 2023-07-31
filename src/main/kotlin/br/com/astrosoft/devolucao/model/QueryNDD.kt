package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.CteXML
import br.com.astrosoft.devolucao.model.beans.NotaEntradaFileXML
import br.com.astrosoft.devolucao.model.nfeXml.ProdutoNotaEntradaVO
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.DatabaseConfig
import br.com.astrosoft.framework.model.QueryDB
import java.time.LocalDate

class QueryNDD : QueryDB(database) {
  fun notasEntrada(dataInicial: LocalDate, process: (bean: List<NotaEntradaVO>) -> Unit) {
    val sql = "/sqlNDD/notasEntrada.sql"
    queryLazy(sql, NotaEntradaVO::class, process) {
      addOptionalParameter("dataInicial", dataInicial)
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

    val ipServer: String? = db.url.split("/").getOrNull(2)

    internal val database = DatabaseConfig(
      driver = db.driver,
      url = db.url,
      user = db.username,
      password = db.password
    )
  }
}

val ndd = QueryNDD()

