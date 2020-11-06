package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.model.beans.UltimasNotas
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB

class QuerySaci: QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
    }.firstOrNull()
  }
  
  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
    }
  }
  
  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("login", user.login)
      addOptionalParameter("bitAcesso", user.bitAcesso)
    }
  }
  
  fun notasDevolucao(): List<NotaDevolucao> {
    val sql = "/sqlSaci/notaDevolucao.sql"
    val dataInicial = 20201001
    return query(sql, NotaDevolucao::class) {
      addOptionalParameter("dataInicial", dataInicial)
    }
  }
  
  fun representante(vendno: Int): List<Representante> {
    val sql = "/sqlSaci/representantes.sql"
    return query(sql, Representante::class) {
      addOptionalParameter("vendno", vendno)
    }
  }
  
  fun produtosNotaSaida(nota: NotaDevolucao): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosNotaSaida.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", nota.loja)
      addOptionalParameter("pdv", nota.pdv)
      addOptionalParameter("transacao", nota.transacao)
    }
  }
  
  fun ultimasNotas() : List<UltimasNotas>{
    val sql = "/sqlSaci/ultimasNotas.sql"
    return emptyList()
    //query(sql, UltimasNotas::class)
  }
  
  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer =
      url.split("/")
        .getOrNull(2)
  }
}

val saci = QuerySaci()