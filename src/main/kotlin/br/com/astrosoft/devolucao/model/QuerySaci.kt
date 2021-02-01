package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.AgendaUpdate
import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.Funcionario
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaEntrada
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.model.beans.UltimasNotas
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.toSaciDate

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
      addOptionalParameter("loja", user.storeno)
    }
  }
  
  fun notasDevolucao(serie: String): List<NotaSaida> {
    val sql = "/sqlSaci/notaDevolucao.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("serie", serie)
    }
  }
  
  fun pedidosDevolucao(): List<NotaSaida> {
    val sql = "/sqlSaci/pedidoDevolucao.sql"
    return query(sql, NotaSaida::class)
  }
  
  fun entradaDevolucao(): List<NotaSaida> {
    val sql = "/sqlSaci/entradaDevolucao.sql"
    return query(sql, NotaSaida::class)
  }
  
  fun produtosPedido(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosPedido.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pedido", notaSaida.pedido)
    }.map {produto ->
      produto.nota = notaSaida
      produto
    }
  }
  
  fun produtosEntrada(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosEntrada.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("invno", notaSaida.transacao)
    }.map {produto ->
      produto.nota = notaSaida
      produto
    }
  }
  
  fun representante(vendno: Int): List<Representante> {
    val sql = "/sqlSaci/representantes.sql"
    return query(sql, Representante::class) {
      addOptionalParameter("vendno", vendno)
    }
  }
  
  fun produtosNotaSaida(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosNotaSaida.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pdv", notaSaida.pdv)
      addOptionalParameter("transacao", notaSaida.transacao)
    }.map {produto ->
      produto.nota = notaSaida
      produto
    }
  }
  
  fun ultimasNotas(): List<UltimasNotas> { //val sql = "/sqlSaci/ultimasNotas.sql"
    return emptyList() //query(sql, UltimasNotas::class)
  }
  
  fun saveRmk(nota: NotaSaida) {
    val sql = "/sqlSaci/rmkUpdate.sql"
    if(nota.tipo == "PED") script(sql) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", 9999)
      addOptionalParameter("xano", nota.pedido)
      addOptionalParameter("rmk", nota.rmk)
    }
    else script(sql) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", nota.pdv)
      addOptionalParameter("xano", nota.transacao)
      addOptionalParameter("rmk", nota.rmk)
    }
  }
  
  fun saveRmkVend(fornecedor: Fornecedor) {
    val sql = "/sqlSaci/rmkUpdateVend.sql"
    script(sql) {
      addOptionalParameter("vendno", fornecedor.vendno)
      addOptionalParameter("tipo", fornecedor.tipo)
      addOptionalParameter("rmk", fornecedor.obs)
    }
  }
  
  //Files
  fun insertFile(file: NFFile) {
    val sql = "/sqlSaci/fileInsert.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
      addOptionalParameter("file", file.file)
    }
  }
  
  fun updateFile(file: NFFile) {
    val sql = "/sqlSaci/fileUpdate.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
      addOptionalParameter("file", file.file)
    }
  }
  
  fun deleteFile(file: NFFile) {
    val sql = "/sqlSaci/fileDelete.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
    }
  }
  
  fun selectFile(nfs: NotaSaida): List<NFFile> {
    val sql = "/sqlSaci/fileSelect.sql"
    return if(nfs.tipo == "PED") query(sql, NFFile::class) {
      addOptionalParameter("storeno", nfs.loja)
      addOptionalParameter("pdvno", 9999)
      addOptionalParameter("xano", nfs.pedido)
    }
    else query(sql, NFFile::class) {
      addOptionalParameter("storeno", nfs.loja)
      addOptionalParameter("pdvno", nfs.pdv)
      addOptionalParameter("xano", nfs.transacao)
    }
  }
  
  //Email
  fun listEmailNota(nota: NotaSaida): List<EmailDB> {
    val sql = "/sqlSaci/listEmailEnviado.sql"
    return if(nota.tipo == "PED") query(sql, EmailDB::class) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", 9999)
      addOptionalParameter("xano", nota.pedido)
    }
    else query(sql, EmailDB::class) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", nota.pdv)
      addOptionalParameter("xano", nota.transacao)
    }
  }
  
  fun listEmailPara(): List<EmailDB> {
    val sql = "/sqlSaci/listEmailEnviadoPara.sql" //return emptyList()
    return query(sql, EmailDB::class)
  }
  
  fun listNotasEmailNota(idEmail: Int): List<EmailDB> {
    val sql = "/sqlSaci/listNotasEmailEnviado.sql"
    return query(sql, EmailDB::class) {
      addOptionalParameter("idEmail", idEmail)
    }
  }
  
  fun salvaEmailEnviado(gmail: EmailGmail, nota: NotaSaida, idEmail: Int) {
    val sql = "/sqlSaci/salvaEmailEnviado.sql"
    val storeno = nota.loja
    val pdvno = if(nota.tipo == "PED") 9999 else nota.pdv
    val xano = if(nota.tipo == "PED") nota.pedido else nota.transacao
    script(sql) {
      addOptionalParameter("idEmail", idEmail)
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("pdvno", pdvno)
      addOptionalParameter("xano", xano)
      addOptionalParameter("email", gmail.email)
      addOptionalParameter("messageID", gmail.messageID)
      addOptionalParameter("assunto", gmail.assunto)
      addOptionalParameter("msg", gmail.msg())
      addOptionalParameter("planilha", gmail.planilha)
      addOptionalParameter("relatorio", gmail.relatorio)
      addOptionalParameter("anexos", gmail.anexos)
    }
  }
  
  fun salvaEmailEnviado(gmail: EmailGmail, idEmail: Int) {
    val sql = "/sqlSaci/salvaEmailEnviado.sql"
    val storeno = 0
    val pdvno = 0
    val xano = 0
    script(sql) {
      addOptionalParameter("idEmail", idEmail)
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("pdvno", pdvno)
      addOptionalParameter("xano", xano)
      addOptionalParameter("email", gmail.email)
      addOptionalParameter("assunto", gmail.assunto)
      addOptionalParameter("messageID", gmail.messageID)
      addOptionalParameter("msg", gmail.msg())
      addOptionalParameter("planilha", gmail.planilha)
      addOptionalParameter("relatorio", gmail.relatorio)
      addOptionalParameter("anexos", gmail.anexos)
    }
  }
  
  fun newEmailId(): Int {
    val sql = "select MAX(idEmail + 1) as max from sqldados.devEmail"
    return query(sql, Max::class).firstOrNull()?.max ?: 1
  }
  
  // Recebimentos
  fun listaNotasPendentes(): List<NotaEntrada> {
    val sql = "/sqlSaci/notasPendentes.sql"
    return query(sql, NotaEntrada::class)
  }
  
  // Agenda
  fun listaAgenda(agendado: Boolean, recebido: Boolean, filtro: String, loja : Int): List<Agenda> {
    val sql = "/sqlSaci/listaAgenda.sql"
    return query(sql, Agenda::class) {
      addOptionalParameter("agendado", if(agendado) "S" else "N")
      addOptionalParameter("recebido", if(recebido) "S" else "N")
      addOptionalParameter("filtro", filtro)
      addOptionalParameter("loja", loja)
    }
  }
  
  fun updateAgenda(agendaUpdate: AgendaUpdate) {
    val sql = "/sqlSaci/updateAgenda.sql"
    val dataStr = agendaUpdate.data.format()
    script(sql) {
      addOptionalParameter("invno", agendaUpdate.invno)
      addOptionalParameter("data", dataStr)
      addOptionalParameter("hora", agendaUpdate.hora ?: "")
      addOptionalParameter("recebedor", agendaUpdate.recebedor ?: "")
      addOptionalParameter("dataRecbedor", agendaUpdate.dataRecbedor.format())
      addOptionalParameter("horaRecebedor", agendaUpdate.horaRecebedor ?: "")
    }
  }
  
  fun listFuncionario(codigo: String): Funcionario? {
    val sql = "/sqlSaci/listFuncionario.sql"
    return query(sql, Funcionario::class) {
      addOptionalParameter("codigo", codigo.toIntOrNull() ?: 0)
    }.firstOrNull()
  }
  
  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    val ipServer = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()

data class Max(val max: Int)