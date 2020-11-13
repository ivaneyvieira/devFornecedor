package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.NFFiles
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.model.beans.UltimasNotas
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.toSaciDate
import java.time.LocalDate

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
  
  fun notasDevolucao(dataInicial: LocalDate?,
                     dataFinal: LocalDate?,
                     fornecedor: String,
                     nota: String): List<NotaSaida> {
    val sql = "/sqlSaci/notaDevolucao.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("dataInicial", dataInicial?.toSaciDate() ?: 0)
      addOptionalParameter("dataFinal", dataFinal?.toSaciDate() ?: 0)
      addOptionalParameter("fornecedor", fornecedor)
      addOptionalParameter("nota", nota)
    }
  }
  
  fun notasVenda(dataInicial: LocalDate?, dataFinal: LocalDate?, fornecedor: String, nota: String): List<NotaSaida> {
    val sql = "/sqlSaci/notaVenda.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("dataInicial", dataInicial?.toSaciDate() ?: 0)
      addOptionalParameter("dataFinal", dataFinal?.toSaciDate() ?: 0)
      addOptionalParameter("fornecedor", fornecedor)
      addOptionalParameter("nota", nota)
    }
  }
  
  fun representante(vendno: Int): List<Representante> {
    val sql = "/sqlSaci/representantes.sql"
    return query(sql, Representante::class) {
      addOptionalParameter("vendno", vendno)
    }
  }
  
  fun produtosNotaSaida(nota: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosNotaSaida.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", nota.loja)
      addOptionalParameter("pdv", nota.pdv)
      addOptionalParameter("transacao", nota.transacao)
    }
  }
  
  fun ultimasNotas(): List<UltimasNotas> {
    //val sql = "/sqlSaci/ultimasNotas.sql"
    return emptyList()
    //query(sql, UltimasNotas::class)
  }
  
  fun saveRmk(nota: NotaSaida) {
    val sql = "/sqlSaci/rmkUpdate.sql"
    script(sql) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", nota.pdv)
      addOptionalParameter("xano", nota.transacao)
      addOptionalParameter("rmk", nota.rmk)
    }
  }
  
  //Files
  fun insertFile(file: NFFiles){
    val sql = "/sqlSaci/fileInsert.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
      addOptionalParameter("data", file.file)
    }
  }
  
  fun updateFile(file: NFFiles){
    val sql = "/sqlSaci/fileUpdate.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
      addOptionalParameter("data", file.file)
    }
  }
  
  fun deleteFile(file: NFFiles){
    val sql = "/sqlSaci/fileDelete.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
    }
  }
  
  fun selectFile(nfs: NotaSaida): List<NFFiles>{
    val sql = "/sqlSaci/fileDelete.sql"
    return query(sql, NFFiles::class) {
      addOptionalParameter("storeno", nfs.loja)
      addOptionalParameter("pdvno", nfs.pdv)
      addOptionalParameter("xano", nfs.transacao)
    }
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