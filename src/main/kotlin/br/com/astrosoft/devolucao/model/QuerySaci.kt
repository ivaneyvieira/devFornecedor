package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.framework.model.Config.appName
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.model.gridlazy.SortOrder
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.toSaciDate
import org.sql2o.Query

class QuerySaci : QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
      addParameter("appName", appName)
    }.firstOrNull()
  }

  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
      addParameter("appName", appName)
    }
  }

  fun allLojas(): List<Loja> {
    val sql = "/sqlSaci/listLojas.sql"
    return query(sql, Loja::class)
  }

  fun findLojaByCnpj(cnpj: String): Loja? {
    val sql = "/sqlSaci/findLojaByCnpj.sql"
    return query(sql, Loja::class) {
      addOptionalParameter("cnpj", cnpj)
    }.firstOrNull()
  }

  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("login", user.login)
      addOptionalParameter("bitAcesso", user.bitAcesso)
      addOptionalParameter("loja", user.storeno)
      addOptionalParameter("appName", appName)
    }
  }

  fun notasDevolucao(serie: Serie): List<NotaSaida> {
    val sql = "/sqlSaci/notaDevolucao.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("serie", serie.value)
    }
  }

  fun pedidosDevolucao(loja: Int): List<NotaSaida> {
    val sql = "/sqlSaci/pedidoDevolucao.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("loja", loja)
    }
  }

  fun entradaDevolucao(): List<NotaSaida> {
    val sql = "/sqlSaci/entradaDevolucao.sql"
    return query(sql, NotaSaida::class)
  }

  fun ajusteGarantia(pago: Boolean): List<NotaSaida> {
    val sql = "/sqlSaci/notaAjusteGarantia.sql"
    return query(sql, NotaSaida::class) {
      this.addOptionalParameter("pago", if (pago) "S" else "N")
    }
  }

  fun notaFinanceiro(): List<NotaSaida> { //val sql = "/sqlSaci/notaFinanceiro.sql"
    //return query(sql, NotaSaida::class)
    return notasDevolucao(Serie.Serie01).filter { nota ->
      nota.chaveDesconto?.trim() != ""
    }
  }

  fun produtosPedido(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosPedido.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pedido", notaSaida.pedido)
    }.map { produto ->
      produto.nota = notaSaida
      produto
    }
  }

  fun produtosEntrada(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosEntrada.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("invno", notaSaida.transacao)
    }.map { produto ->
      produto.nota = notaSaida
      produto
    }
  }

  fun produtosAjuste(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosAjuste.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pdv", notaSaida.pdv)
      addOptionalParameter("transacao", notaSaida.transacao)
    }.map { produto ->
      produto.nota = notaSaida
      produto
    }
  }

  fun produtosFinanceiro(notaSaida: NotaSaida): List<ProdutosNotaSaida> {
    val sql = "/sqlSaci/produtosFinanceiro.sql"
    return query(sql, ProdutosNotaSaida::class) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pdv", notaSaida.pdv)
      addOptionalParameter("transacao", notaSaida.transacao)
    }.map { produto ->
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
    }.map { produto ->
      produto.nota = notaSaida
      produto
    }
  }

  fun saveRmk(nota: NotaSaida) {
    val sql = "/sqlSaci/rmkUpdate.sql"
    if (nota.tipo == "PED") script(sql) {
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
    return if (nfs.tipo == "PED") query(sql, NFFile::class) {
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
    return if (nota.tipo == "PED") query(sql, EmailDB::class) {
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
    val pdvno = if (nota.tipo == "PED") 9999 else nota.pdv
    val xano = if (nota.tipo == "PED") nota.pedido else nota.transacao
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
  fun listaAgenda(agendado: Boolean, recebido: Boolean, filtro: String, loja: Int): List<Agenda> {
    val sql = "/sqlSaci/listaAgenda.sql"
    return query(sql, Agenda::class) {
      addOptionalParameter("agendado", if (agendado) "S" else "N")
      addOptionalParameter("recebido", if (recebido) "S" else "N")
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

  fun listParcelasFornecedor(vendno: Int): List<Parcela> {
    val sql = "/sqlSaci/parcelasFornecedor.sql"
    return query(sql, Parcela::class) {
      addOptionalParameter("vendno", vendno)
    }
  }

  fun listPedidosFornecedor(vendno: Int): List<Pedido> {
    val sql = "/sqlSaci/pedidosFornecedor.sql"
    return query(sql, Pedido::class) {
      addOptionalParameter("vendno", vendno)
    }
  }

  fun deleteFornecedorSap() {
    val sql = "/sqlSaci/deleteFornecedorSap.sql"
    script(sql)
  }

  fun saveFornecedorSap(fornecedores: FornecedorSap) {
    val sql = "/sqlSaci/saveFornecedorSap.sql"
    script(sql) {
      addOptionalParameter("codigo", fornecedores.codigo)
      addOptionalParameter("nome", fornecedores.nome)
    }
    fornecedores.notas.forEach { nota ->
      saveNotaSap(fornecedores.codigo, nota)
    }
  }

  fun listNotaSap(filtro: String): List<NotaDevolucaoSap> {
    val sql = "/sqlSaci/listNotaSap.sql"
    return query(sql, NotaDevolucaoSap::class) {
      addOptionalParameter("filtro", filtro)
    }
  }

  fun findFornecedor(cnpj: String): FornecedorSaci? {
    val sql = "/sqlSaci/findFornecedor.sql"
    return query(sql, FornecedorSaci::class) {
      addOptionalParameter("cnpj", cnpj)
    }.firstOrNull()
  }

  private fun saveNotaSap(codigoFor: Int, nota: NotaDevolucaoSap) {
    val sql = "/sqlSaci/saveNotaSap.sql"
    script(sql) {
      addOptionalParameter("codigoFor", codigoFor)
      addOptionalParameter("numero", nota.numero)
      addOptionalParameter("storeno", nota.storeno)
      addOptionalParameter("dataLancamento", nota.dataLancamento)
      addOptionalParameter("dataVencimento", nota.dataVencimento)
      addOptionalParameter("saldo", nota.saldo)
    }
  }

  fun saveNotaNdd(notas: List<NotaEntradaVO>) {
    val sql = "/sqlSaci/saveNotaNdd.sql"
    script(sql, notas.map { nota ->
      { q: Query ->
        q.addOptionalParameter("id", nota.id)
        q.addOptionalParameter("numero", nota.numero)
        q.addOptionalParameter("serie", nota.serie)
        q.addOptionalParameter("dataEmissao", nota.dataEmissao?.toSaciDate() ?: 0)
        q.addOptionalParameter("cnpjEmitente", nota.cnpjEmitente)
        q.addOptionalParameter("nomeFornecedor", nota.nomeFornecedor)
        q.addOptionalParameter("cnpjDestinatario", nota.cnpjDestinatario)
        q.addOptionalParameter("ieEmitente", nota.ieEmitente)
        q.addOptionalParameter("ieDestinatario", nota.ieDestinatario)
        q.addOptionalParameter("baseCalculoIcms", nota.valorNota ?: nota.baseCalculoIcms)
        q.addOptionalParameter("baseCalculoSt", nota.baseCalculoSt)
        q.addOptionalParameter("valorTotalProdutos", nota.valorTotalProdutos)
        q.addOptionalParameter("valorTotalIcms", nota.valorTotalIcms)
        q.addOptionalParameter("valorTotalSt", nota.valorTotalSt)
        q.addOptionalParameter("baseCalculoIssqn", nota.baseCalculoIssqn)
        q.addOptionalParameter("chave", nota.chave)
        q.addOptionalParameter("status", nota.status)
        q.addOptionalParameter("xmlAut", nota.xmlAut)
        q.addOptionalParameter("xmlCancelado", nota.xmlCancelado)
        q.addOptionalParameter("xmlNfe", nota.xmlNfe)
        q.addOptionalParameter("xmlDadosAdicionais", nota.xmlDadosAdicionais)
      }
    })
  }

  fun notasEntrada(filtro: FiltroEntradaNdd): List<NotaEntradaNdd> {
    val sql = "/sqlSaci/notasEntrada.sql"
    return query(sql, NotaEntradaNdd::class) {
      addOptionalParameter("filtro", filtro.query)
      addOptionalParameter("tipo", filtro.tipo.toString())
      addOptionalParameter("dataInicial", filtro.dataInicial)
      addOptionalParameter("dataFinal", filtro.dataFinal)
      addOptionalParameter("chave", filtro.chave)
    }
  }

  fun saveNotaNddPedido(nota: NotaEntradaNdd) {
    val sql = "/sqlSaci/saveNotaNddPedido.sql"
    script(sql) {
      addOptionalParameter("id", nota.id)
      addOptionalParameter("ordno", nota.ordno)
    }
  }

  fun ultimasNotasEntrada(filtro: FiltroUltimaNotaEntrada): List<UltimaNotaEntrada> {
    val sql = "/sqlSaci/ultimasNotasEntradaFetch.sql"
    return query(sql, UltimaNotaEntrada::class) {
      addOptionalParameter("storeno", filtro.storeno)
      addOptionalParameter("di", filtro.di.toSaciDate())
      addOptionalParameter("df", filtro.df.toSaciDate())
      addOptionalParameter("vendno", filtro.vendno)
      addOptionalParameter("ni", filtro.ni)
      addOptionalParameter("nf", filtro.nf)
      addOptionalParameter("prd", filtro.prd)
      addOptionalParameter("cst", filtro.cst.str)
      addOptionalParameter("icms", filtro.icms.str)
      addOptionalParameter("ipi", filtro.ipi.str)
      addOptionalParameter("mva", filtro.mva.str)
      addOptionalParameter("ncm", filtro.ncm.str)
    }
  }

  fun queryUltimaNota(filter: FiltroUltimaNotaEntrada) {
    val sql = if (filter.ultimaNota) "/sqlSaci/ultimasNotasEntradaQueryUtm.sql"
    else "/sqlSaci/ultimasNotasEntradaQuery.sql"
    script(sql) {
      addOptionalParameter("storeno", filter.storeno)
      addOptionalParameter("di", filter.di.toSaciDate())
      addOptionalParameter("df", filter.df.toSaciDate())
      addOptionalParameter("vendno", filter.vendno)
      addOptionalParameter("mfno", filter.mfno)
      addOptionalParameter("ni", filter.ni)
      addOptionalParameter("nf", filter.nf)
      addOptionalParameter("prd", filter.prd)
      addOptionalParameter("cst", filter.cst.str)
      addOptionalParameter("icms", filter.icms.str)
      addOptionalParameter("ipi", filter.ipi.str)
      addOptionalParameter("mva", filter.mva.str)
      addOptionalParameter("ncm", filter.ncm.str)
      addOptionalParameter("rotulo", filter.rotulo)
    }
  }

  private fun <R : Any> filtroUltimaNota(filter: FiltroUltimaNotaEntrada,
                                         sql: String,
                                         complemento: String? = null,
                                         result: (Query) -> R): R {
    return querySerivce(sql, complemento, lambda = {
      addOptionalParameter("storeno", filter.storeno)
      addOptionalParameter("di", filter.di.toSaciDate())
      addOptionalParameter("df", filter.df.toSaciDate())
      addOptionalParameter("vendno", filter.vendno)
      addOptionalParameter("ni", filter.ni)
      addOptionalParameter("nf", filter.nf)
      addOptionalParameter("prd", filter.prd)
      addOptionalParameter("cst", filter.cst.str)
      addOptionalParameter("icms", filter.icms.str)
      addOptionalParameter("ipi", filter.ipi.str)
      addOptionalParameter("mva", filter.mva.str)
      addOptionalParameter("ncm", filter.ncm.str)
    }, result = result)
  }

  fun countUltimaNota(filter: FiltroUltimaNotaEntrada): Int {
    val sql = "/sqlSaci/ultimasNotasEntradaCount.sql"
    return filtroUltimaNota(filter, sql) {
      it.executeScalar(Int::class.java)
    }
  }

  fun fetchUltimaNota(
    filter: FiltroUltimaNotaEntrada,
    offset: Int,
    limit: Int,
    sortOrders: List<SortOrder>,
                     ): List<UltimaNotaEntrada> {
    val sql = "/sqlSaci/ultimasNotasEntradaFetch.sql"
    val orderBy = if (sortOrders.isEmpty()) ""
    else "ORDER BY " + sortOrders.joinToString(separator = ", ") { it.sql() }
    val complemento = """
      |$orderBy 
      |LIMIT $limit OFFSET $offset""".trimMargin()
    return filtroUltimaNota(filter, sql, complemento) {
      it.executeAndFetch(UltimaNotaEntrada::class.java)
    }
  }

  fun notaSaidaNDD(filtro: FiltroNotaSaidaNdd): List<NotaSaidaNdd> {
    val sql = "/sqlSaci/notaSaida.sql"
    val dataI = filtro.dataI?.toSaciDate() ?: 20000101
    val dataF = filtro.dataF?.toSaciDate() ?: 30000101
    return query(sql, NotaSaidaNdd::class) {
      addOptionalParameter("loja", filtro.loja ?: 0)
      addOptionalParameter("numero", filtro.numero ?: 0)
      addOptionalParameter("serie", filtro.serie ?: "")
      addOptionalParameter("dataI", dataI)
      addOptionalParameter("dataF", dataF)
      addOptionalParameter("codigoCliente", filtro.codigoCliente ?: 0)
      addOptionalParameter("nomeCliente", filtro.nomeCliente ?: "")
    }
  }

  fun salvaDesconto(notaSaida: NotaSaida) {
    val sql = "/sqlSaci/notaSaidaUpdateDesconto.sql"
    script(sql) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pdv", notaSaida.pdv)
      addOptionalParameter("transacao", notaSaida.transacao)
      addOptionalParameter("chaveDesconto", notaSaida.chaveDesconto)
    }
  }

  fun todasNotasEntradaQuery(filtro: FiltroNotaEntradaQuery): List<NotaEntradaQuery> {
    val sql = "/sqlSaci/todasNotasEntradaQuery.sql"
    return query(sql, NotaEntradaQuery::class) {
      addOptionalParameter("storeno", filtro.storeno)
      addOptionalParameter("di", filtro.di.toSaciDate())
      addOptionalParameter("df", filtro.df.toSaciDate())
      addOptionalParameter("vendno", filtro.vendno)
      addOptionalParameter("mfno", filtro.mfno)
      addOptionalParameter("ni", filtro.ni)
      addOptionalParameter("nf", filtro.nf)
      addOptionalParameter("prd", filtro.prd)
    }
  }

  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    val ipServer: String? = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()

data class Max(val max: Int)