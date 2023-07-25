package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.framework.model.Config.appName
import br.com.astrosoft.framework.model.DB
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.toSaciDate
import org.sql2o.Query
import java.time.LocalDate

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
      addOptionalParameter("senhaPrint", user.senhaPrint)
      addOptionalParameter("bitAcesso", user.bitAcesso)
      addOptionalParameter("loja", user.storeno)
      addOptionalParameter("appName", appName)
    }
  }

  fun notasDevolucao(serie: Serie, vendno: Int = 0): List<NotaSaida> {
    val sql = "/sqlSaci/notaDevolucao.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("serie", serie.value)
      addOptionalParameter("vendno", vendno)
    }
  }

  fun notasNFD(vendno: Int = 0): List<NotaSaida> {
    val sql = "/sqlSaci/notaNFD.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("vendno", vendno)
    }
  }

  fun notasRetorno(vendno: Int = 0): List<NotaSaida> {
    val sql = "/sqlSaci/notaRetorno.sql"
    return query(sql, NotaSaida::class) {
      addOptionalParameter("vendno", vendno)
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

  fun descontoDevolucao(filtro: FiltroFornecedor): List<NotaEntradaDesconto> {
    val sql = "/sqlSaci/descontoDevolucao.sql"
    return query(sql, NotaEntradaDesconto::class) {
      addOptionalParameter("filtro", filtro.query)
      addOptionalParameter("loja", filtro.loja.no)
    }
  }

  fun ajusteGarantia(serie: Serie): List<NotaSaida> {
    val sql = "/sqlSaci/notaAjusteGarantia.sql"
    val serieValue = if (serie in listOf(AJT, AJD, AJP, AJC)) serie.value else return emptyList()
    val notas = query(sql, NotaSaida::class) {
      this.addOptionalParameter("TIPO_NOTA", serieValue)
    }
    return notas
  }

  fun ajusteGarantia66(serie: Serie): List<NotaSaida> {
    val sql = "/sqlSaci/notaAjusteGarantia66.sql"
    val serieValue = if (serie in listOf(AJT, AJD, AJP, AJC)) serie.value else return emptyList()
    val notas = query(sql, NotaSaida::class) {
      this.addOptionalParameter("TIPO_NOTA", serieValue)
    }
    return notas
  }

  fun notaFinanceiro(): List<NotaSaida> {
    return notasDevolucao(Serie01).filter { nota ->
      nota.isObservacaoFinanceiro()
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

  fun selectFile(pedido: PedidoCompra, pdvno: Int): List<NFFile> {
    val sql = "/sqlSaci/fileSelect.sql"
    return query(sql, NFFile::class) {
      addOptionalParameter("storeno", pedido.loja)
      addOptionalParameter("pdvno", pdvno)
      addOptionalParameter("xano", pedido.numeroPedido)
    }
  }

  fun selectFile(nota: FornecedorNota): List<NFFile> {
    val sql = "/sqlSaci/fileSelect.sql"
    return query(sql, NFFile::class) {
      addOptionalParameter("storeno", 77)
      addOptionalParameter("pdvno", 7777)
      addOptionalParameter("xano", nota.ni)
    }
  }

  fun selectFile(fornecedor: FornecedorProduto): List<NFFile> {
    val sql = "/sqlSaci/fileSelect.sql"
    return query(sql, NFFile::class) {
      addOptionalParameter("storeno", 88)
      addOptionalParameter("pdvno", 8888)
      addOptionalParameter("xano", fornecedor.vendno)
    }
  }

  fun selectFile(agenda: AgendaDemanda): List<NFFile> {
    val sql = "/sqlSaci/fileSelect.sql"
    return query(sql, NFFile::class) {
      addOptionalParameter("storeno", 1)
      addOptionalParameter("pdvno", 8888)
      addOptionalParameter("xano", agenda.id)
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
    val coleta = agendaUpdate.coleta?.toSaciDate()?.toString() ?: ""
    script(sql) {
      addOptionalParameter("invno", agendaUpdate.invno)
      addOptionalParameter("data", dataStr)
      addOptionalParameter("coleta", coleta)
      addOptionalParameter("hora", agendaUpdate.hora ?: "")
      addOptionalParameter("recebedor", agendaUpdate.recebedor ?: "")
      addOptionalParameter("conhecimento", agendaUpdate.conhecimento ?: "")
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

  fun findXmlNfe(ni: Int): NddXml? {
    val sql = "/sqlSaci/notasEntradaXml.sql"
    return query(sql, NddXml::class) {
      addOptionalParameter("ni", ni)
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

  fun dataInicialNdd(): LocalDate {
//    val sql = "/sqlSaci/dataInicialNdd.sql"
    return LocalDate.now()
      .minusDays(45)// if (data > dataAntiga.toSaciDate()) dataAntiga else LocalDate.now().minusMonths(1)
  }

  fun saveNotaNdd(notas: List<NotaEntradaVO>) {
    val sql = "/sqlSaci/saveNotaNdd.sql"
    script(sql, notas.map { nota ->
      {
        addOptionalParameter("id", nota.id)
        addOptionalParameter("numero", nota.numero)
        addOptionalParameter("cancelado", nota.cancelado)
        addOptionalParameter("serie", nota.serie)
        addOptionalParameter("dataEmissao", nota.dataEmissao?.toSaciDate() ?: 0)
        addOptionalParameter("cnpjEmitente", nota.cnpjEmitente)
        addOptionalParameter("nomeFornecedor", nota.nomeFornecedor)
        addOptionalParameter("cnpjDestinatario", nota.cnpjDestinatario)
        addOptionalParameter("ieEmitente", nota.ieEmitente)
        addOptionalParameter("ieDestinatario", nota.ieDestinatario)
        addOptionalParameter("baseCalculoIcms", nota.valorNota ?: nota.baseCalculoIcms)
        addOptionalParameter("baseCalculoSt", nota.baseCalculoSt)
        addOptionalParameter("valorTotalProdutos", nota.valorTotalProdutos)
        addOptionalParameter("valorTotalIcms", nota.valorTotalIcms)
        addOptionalParameter("valorTotalSt", nota.valorTotalSt)
        addOptionalParameter("baseCalculoIssqn", nota.baseCalculoIssqn)
        addOptionalParameter("chave", nota.chave)
        addOptionalParameter("status", nota.status)
        addOptionalParameter("xmlAut", nota.xmlAut)
        addOptionalParameter("xmlCancelado", nota.xmlCancelado)
        addOptionalParameter("xmlNfe", nota.xmlNfe)
        addOptionalParameter("xmlDadosAdicionais", nota.xmlDadosAdicionais)
      }
    })
    notas.forEach { nota ->
      addProdutosNdd(ProdutosNdd.fromNdd(nota))
    }
  }

  fun notasEntrada(filtro: FiltroEntradaNdd): List<NotaEntradaNdd> {
    val sql = "/sqlSaci/notasEntrada.sql"
    return query(sql, NotaEntradaNdd::class) {
      addOptionalParameter("filtro", filtro.query)
      addOptionalParameter("tipo", filtro.tipo.toString())
      addOptionalParameter("dataInicial", filtro.dataInicial.toSaciDate())
      addOptionalParameter("dataFinal", filtro.dataFinal.toSaciDate())
      addOptionalParameter("chave", filtro.chave)
      addOptionalParameter("temIPI", filtro.temIPI.toString())
    }
  }

  fun notasEntradaSaci(filtro: FiltroEntradaSaci): List<NotaEntradaSaci> {
    val sql = "/sqlSaci/notasEntrada.sql"
    return query(sql, NotaEntradaSaci::class) {
      addOptionalParameter("filtro", filtro.query)
      addOptionalParameter("tipo", "TODOS")
      addOptionalParameter("dataInicial", filtro.dataInicial.toSaciDate())
      addOptionalParameter("dataFinal", filtro.dataFinal.toSaciDate())
      addOptionalParameter("chave", filtro.chave)
      addOptionalParameter("temIPI", "TODOS")
    }
  }

  fun saveNotaNddPedido(nota: NotaEntradaNdd) {
    val sql = "/sqlSaci/saveNotaNddPedido.sql"
    script(sql) {
      addOptionalParameter("id", nota.id)
      addOptionalParameter("ordno", nota.ordno)
    }
  }


  fun fornecedorNotas(filtro: FiltroFornecedorNota): List<FornecedorNota> {
    val sql = "/sqlSaci/fornecedorNota.sql"
    return query(sql, FornecedorNota::class) {
      addOptionalParameter("vendno", filtro.vendno)
      addOptionalParameter("loja", filtro.loja)
      addOptionalParameter("query", filtro.query)
    }
  }


  fun ultimasNfPrec(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    val sql = "/sqlSaci/ultimasNotasEntradaFetch.sql"
    return query(sql, NfPrecEntrada::class) {
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
      addOptionalParameter("barcode", filtro.barcode.str)
      addOptionalParameter("refPrd", filtro.refPrd.str)
      addOptionalParameter("frete", filtro.frete.str)
      addOptionalParameter("fretePer", filtro.fretePer.str)
      addOptionalParameter("preco", filtro.preco.str)
      addOptionalParameter("pesquisa", filtro.pesquisa)
    }
  }

  fun ultimasPreRecebimento(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    val sql = "/sqlSaci/ultimasPreRecebimentoFetch.sql"
    return query(sql, NfPrecEntrada::class) {
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
      addOptionalParameter("barcode", filtro.barcode.str)
      addOptionalParameter("refPrd", filtro.refPrd.str)
      addOptionalParameter("frete", filtro.frete.str)
      addOptionalParameter("fretePer", filtro.fretePer.str)
      addOptionalParameter("preco", filtro.preco.str)
      addOptionalParameter("pesquisa", filtro.pesquisa)
    }
  }

  fun queryNfPrec(filter: FiltroRelatorio) {
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
      addOptionalParameter("barcode", filter.barcode.str)
      addOptionalParameter("refPrd", filter.refPrd.str)
      addOptionalParameter("frete", filter.frete.str)
      addOptionalParameter("fretePer", filter.fretePer.str)
      addOptionalParameter("rotulo", filter.rotulo)
      addOptionalParameter("comGrade", if (filter.comGrade) "S" else "N")
      addOptionalParameter("listaProdutos", filter.listaProdutos)
    }
  }

  fun queryCte(filter: FiltroNFEntradaFrete) {
    val sql = "/sqlSaci/createNFCte.sql"

    script(sql) {
      addOptionalParameter("loja", filter.loja)
      addOptionalParameter("di", filter.di.toSaciDate())
      addOptionalParameter("df", filter.df.toSaciDate())
      addOptionalParameter("vend", filter.vend)
      addOptionalParameter("ni", filter.ni)
      addOptionalParameter("nfno", filter.nfno)
      addOptionalParameter("carrno", filter.carrno)
      addOptionalParameter("niCte", filter.niCte)
      addOptionalParameter("cte", filter.cte)
      addOptionalParameter("tabno", filter.tabno)
    }
  }

  fun queryPreRecebimento(filter: FiltroRelatorio) {
    val sql = if (filter.ultimaNota) "/sqlSaci/ultimasPreRecebimentoQueryUtm.sql"
    else "/sqlSaci/ultimasPreRecebimentoQuery.sql"
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
      addOptionalParameter("barcode", filter.barcode.str)
      addOptionalParameter("refPrd", filter.refPrd.str)
      addOptionalParameter("frete", filter.frete.str)
      addOptionalParameter("fretePer", filter.fretePer.str)
      addOptionalParameter("rotulo", filter.rotulo)
      addOptionalParameter("comGrade", if (filter.comGrade) "S" else "N")
      addOptionalParameter("listaProdutos", filter.listaProdutos)
    }
  }

  private fun <R : Any> filtroNfPrec(
    filter: FiltroRelatorio, sql: String, complemento: String? = null, result: (Query) -> R
  ): R {
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
      addOptionalParameter("barcode", filter.barcode.str)
      addOptionalParameter("refPrd", filter.refPrd.str)
      addOptionalParameter("frete", filter.frete.str)
      addOptionalParameter("fretePer", filter.fretePer.str)
      addOptionalParameter("preco", filter.preco.str)
      addOptionalParameter("pesquisa", filter.pesquisa)
    }, result = result)
  }

  fun countNfPrec(filter: FiltroRelatorio): Int {
    val sql = "/sqlSaci/ultimasNotasEntradaCount.sql"
    return filtroNfPrec(filter, sql) {
      it.executeScalar(Int::class.java)
    }
  }

  fun countPreRecebimento(filter: FiltroRelatorio): Int {
    val sql = "/sqlSaci/ultimasPreRecebimentoCount.sql"
    return filtroNfPrec(filter, sql) {
      it.executeScalar(Int::class.java)
    }
  }

  fun notaSaidaNDD(filtro: FiltroNotaSaidaNdd): List<NotaSaidaNdd> {
    val sql = "/sqlSaci/notaSaida.sql"
    val dataI = filtro.dataI?.toSaciDate() ?: filtro.dataF?.toSaciDate()
    val dataF = filtro.dataF?.toSaciDate() ?: dataI
    return query(sql, NotaSaidaNdd::class) {
      addOptionalParameter("loja", filtro.loja ?: 0)
      addOptionalParameter("numero", filtro.numero ?: 0)
      addOptionalParameter("serie", filtro.serie ?: "")
      addOptionalParameter("dataI", dataI ?: 20000101)
      addOptionalParameter("dataF", dataF ?: 30000101)
      addOptionalParameter("codigoCliente", filtro.codigoCliente ?: 0)
      addOptionalParameter("nomeCliente", filtro.nomeCliente ?: "")
    }
  }

  fun salvaDesconto(notaSaida: NotaSaida) {
    if (notaSaida.tipo == "PED") salvaDescontoPed(notaSaida)
    else salvaDescontoNota(notaSaida)
  }

  private fun salvaDescontoPed(notaSaida: NotaSaida) {
    val sql = "/sqlSaci/notaSaidaUpdateDescontoPed.sql"
    script(sql) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pedido", notaSaida.pedido)
      addOptionalParameter("chaveDesconto", notaSaida.chaveDesconto)
      addOptionalParameter("observacaoAuxiliar", notaSaida.observacaoAuxiliar)
      addOptionalParameter("pedidos", notaSaida.pedidos ?: "")
      addOptionalParameter("dataAgenda", notaSaida.dataAgenda?.toSaciDate() ?: 0)
    }
  }

  private fun salvaDescontoNota(notaSaida: NotaSaida) {
    val sql = "/sqlSaci/notaSaidaUpdateDesconto.sql"
    script(sql) {
      addOptionalParameter("loja", notaSaida.loja)
      addOptionalParameter("pdv", notaSaida.pdv)
      addOptionalParameter("transacao", notaSaida.transacao)
      addOptionalParameter("chaveDesconto", notaSaida.chaveDesconto)
      addOptionalParameter("observacaoAuxiliar", notaSaida.observacaoAuxiliar)
      addOptionalParameter("pedidos", notaSaida.pedidos ?: "")
      addOptionalParameter("dataAgenda", notaSaida.dataAgenda?.toSaciDate() ?: 0)
      addOptionalParameter("nfAjuste", notaSaida.nfAjuste)
      addOptionalParameter("dataNfAjuste", notaSaida.dataNfAjuste?.toSaciDate() ?: 0)
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
      addOptionalParameter("listaProdutos", filtro.listaProdutos)
    }
  }

  fun fornecedorProduto(filtro: String): List<FornecedorProduto> {
    val sql = "/sqlSaci/fornecdorProduto.sql"
    return query(sql, FornecedorProduto::class) {
      addOptionalParameter("filtro", filtro)
    }
  }

  fun findReimpressao(filtro: FiltroReimpressao): List<ReimpressaoNota> {
    val sql = "/sqlSaci/selectReimpressaoNota.sql"
    return query(sql, ReimpressaoNota::class) {
      addOptionalParameter("filtro", filtro.filtro)
      addOptionalParameter("loja", filtro.loja)
    }
  }

  fun findReimpressao(loja: Int, nota: String, login: String): List<ReimpressaoNota> {
    val sql = "/sqlSaci/selectReimpressaoNotaNota.sql"
    return query(sql, ReimpressaoNota::class) {
      addOptionalParameter("loja", loja)
      addOptionalParameter("numero", nota)
      addOptionalParameter("login", login)
    }
  }

  fun getQuantidadeAvaria(produtoNotaEntradaNdd: ProdutoNotaEntradaNdd): Double? {
    val sql = "/sqlSaci/getQuantidadeAvaria.sql"
    return query(sql, QuantidadeAvaria::class) {
      addOptionalParameter("id", produtoNotaEntradaNdd.id)
      addOptionalParameter("numeroProtocolo", produtoNotaEntradaNdd.numeroProtocolo)
      addOptionalParameter("codigo", produtoNotaEntradaNdd.codigo)
      addOptionalParameter("codBarra", produtoNotaEntradaNdd.codBarra)
    }.firstOrNull()?.quantidade
  }

  fun setQuantidadeAvaria(produtoNotaEntradaNdd: ProdutoNotaEntradaNdd, value: Double?) {
    val sql = "/sqlSaci/setQuantidadeAvaria.sql"
    script(sql) {
      addOptionalParameter("id", produtoNotaEntradaNdd.id)
      addOptionalParameter("numeroProtocolo", produtoNotaEntradaNdd.numeroProtocolo)
      addOptionalParameter("codigo", produtoNotaEntradaNdd.codigo)
      addOptionalParameter("codBarra", produtoNotaEntradaNdd.codBarra)
      addOptionalParameter("quantidade", value ?: 0.00)
    }
  }

  fun findPedidosCompraProduto(filtro: FiltroPedidoCompra): List<PedidoCompraProduto> {
    val sql = "/sqlSaci/listComprasProduto.sql"
    return query(sql, PedidoCompraProduto::class) {
      addOptionalParameter("loja", filtro.loja)
      addOptionalParameter("pesquisa", filtro.pesquisa)
      addOptionalParameter("onlyPendente", filtro.onlyPendente.let { onlyPendente ->
        if (onlyPendente) "S" else "N"
      })
      addOptionalParameter("onlyConfirmado", filtro.onlyConferido.let { onlyConfirmado ->
        if (onlyConfirmado) "S" else "N"
      })
      addOptionalParameter("onlyNotConfirmado", filtro.onlyNotConferido.let { onlyNotConfirmado ->
        if (onlyNotConfirmado) "S" else "N"
      })
      addOptionalParameter("dataPedido", filtro.dataPedido?.toSaciDate() ?: 0)
    }
  }

  fun updateConferido(pedidoCompraProduto: PedidoCompraProduto) {/*
    Chave loja, numeroPedido, codigo, grade, seqno*/
    val sql = "/sqlSaci/updateComprasProduto.sql"
    script(sql) {
      addOptionalParameter("loja", pedidoCompraProduto.loja)
      addOptionalParameter("numeroPedido", pedidoCompraProduto.numeroPedido)
      addOptionalParameter("codigo", pedidoCompraProduto.codigo)
      addOptionalParameter("grade", pedidoCompraProduto.grade)
      addOptionalParameter("seqno", pedidoCompraProduto.seqno ?: 0)
      addOptionalParameter("confirmado", pedidoCompraProduto.confirmado)
      addOptionalParameter("calcEmbalagem", pedidoCompraProduto.calcEmbalagem)
    }
  }

  /*CRUD da tabela Agenda Demanda*/

  fun selectAgendaDemanda(filter: FilterAgendaDemanda): List<AgendaDemanda> {
    val sql = "/sqlSaci/demandasSelect.sql"
    return query(sql, AgendaDemanda::class) {
      addOptionalParameter("pesquisa", filter.pesquisa)
      addOptionalParameter("concluido", filter.concluido?.let { if (it) "S" else "N" } ?: "")
    }
  }

  fun deleteAgendaDemanda(agendaDemanda: AgendaDemanda) {
    val sql = "/sqlSaci/demandasDelete.sql"
    script(sql) {
      addOptionalParameter("id", agendaDemanda.id)
    }
  }

  fun updateAgendaDemanda(agendaDemanda: AgendaDemanda) {
    val sql = "/sqlSaci/demandasUpdate.sql"
    script(sql) {
      addOptionalParameter("id", agendaDemanda.id)
      addOptionalParameter("titulo", agendaDemanda.titulo)
      addOptionalParameter("date", agendaDemanda.date.toSaciDate())
      addOptionalParameter("conteudo", agendaDemanda.conteudo)
      addOptionalParameter("concluido", agendaDemanda.concluido)
      addOptionalParameter("vendno", agendaDemanda.vendno)
      addOptionalParameter("destino", agendaDemanda.destino)
      addOptionalParameter("origem", agendaDemanda.origem)
    }
  }

  fun insertAgendaDemanda(agendaDemanda: AgendaDemanda) {
    val sql = "/sqlSaci/demandasInsert.sql"
    script(sql) {
      addOptionalParameter("titulo", agendaDemanda.titulo)
      addOptionalParameter("date", agendaDemanda.date.toSaciDate())
      addOptionalParameter("conteudo", agendaDemanda.conteudo)
      addOptionalParameter("vendno", agendaDemanda.vendno)
      addOptionalParameter("destino", agendaDemanda.destino)
      addOptionalParameter("origem", agendaDemanda.origem)
    }
  }

  fun findNotasEntradaCte(filter: FiltroDialog): List<NfEntradaFrete> {
    val sql = "/sqlSaci/listCte.sql"
    return query(sql, NfEntradaFrete::class) {
      addOptionalParameter("status", filter.status.cod)
      addOptionalParameter("diferenca", filter.diferenca.cod)
    }
  }

  fun findTabName(carrno: Int, tabelano: Int): TabelaFrete? {
    val sql = "/sqlSaci/findTabFrete.sql"

    return query(sql, TabelaFrete::class) {
      addOptionalParameter("carrno", carrno)
      addOptionalParameter("tabelano", tabelano)
    }.firstOrNull()
  }

  fun saveVendComplemento(fornecedor: FornecedorProduto) {
    val sql = "/sqlSaci/saveVendComplemento.sql"

    script(sql) {
      addOptionalParameter("vendno", fornecedor.vendno)
      addOptionalParameter("texto", fornecedor.texto)
    }
  }

  fun listNFEntrada(filter: FiltroNotaEntradaXML): List<NotaEntradaXML> {
    val sql = "/sqlSaci/listNFEntrada.sql"
    return query(sql, NotaEntradaXML::class) {
      addOptionalParameter("loja", filter.loja?.no ?: 0)
      addOptionalParameter("dataInicial", filter.dataInicial.toSaciDate())
      addOptionalParameter("dataFinal", filter.dataFinal.toSaciDate())
      addOptionalParameter("numero", filter.numero)
      addOptionalParameter("cnpj", filter.cnpj)
      addOptionalParameter("fornecedor", filter.fornecedor)
    }
  }

  fun listPrdRef(codigo: String, grade: String): List<PrdRef> {
    val sql = "/sqlSaci/prdRefSelect.sql"
    return query(sql, PrdRef::class) {
      addOptionalParameter("codigo", codigo)
      addOptionalParameter("grade", grade)
    }
  }

  fun addPrdRef(listPrdRef: List<PrdRef>) {
    val sql = "/sqlSaci/prdRefInsert.sql"
    script(sql, listPrdRef.map { prdRef ->
      { q: Query ->
        q.addOptionalParameter("codigo", prdRef.codigo)
        q.addOptionalParameter("grade", prdRef.grade)
        q.addOptionalParameter("prdrefno", prdRef.prdrefno)
      }
    })
  }

  fun addPrdBar(listPrdBar: List<PrdBar>) {
    val sql = "/sqlSaci/prdBarInsert.sql"
    script(sql, listPrdBar.map { prdBar ->
      { q: Query ->
        q.addOptionalParameter("codigo", prdBar.codigo)
        q.addOptionalParameter("grade", prdBar.grade)
        q.addOptionalParameter("barcode", prdBar.barcode)
      }
    })
  }

  fun addNCM(codigo: String, ncm: String) {
    val sql = "/sqlSaci/ncmInsert.sql"
    script(sql) {
      addOptionalParameter("codigo", codigo)
      addOptionalParameter("ncm", ncm)
    }
  }

  fun addProdutosNdd(produtos: List<ProdutosNdd>) {
    val sql = "/sqlSaci/insertProdutoNdd.sql"

    script(sql, lambda = produtos.map { produto ->
      {
        addOptionalParameter("id", produto.id)
        addOptionalParameter("nItem", produto.nItem)
        addOptionalParameter("cProd", produto.cProd)
        addOptionalParameter("cEAN", produto.cEAN)
        addOptionalParameter("cEANTrib", produto.cEANTrib)
        addOptionalParameter("xProd", produto.xProd)
        addOptionalParameter("ncm", produto.ncm)
        addOptionalParameter("cfop", produto.cfop)
        addOptionalParameter("uCom", produto.uCom)
        addOptionalParameter("qCom", produto.qCom)
        addOptionalParameter("vUnCom", produto.vUnCom)
        addOptionalParameter("vProd", produto.vProd)
        addOptionalParameter("indTot", produto.indTot)
        addOptionalParameter("cstIcms", produto.cstIcms)
        addOptionalParameter("baseIcms", produto.baseIcms)
        addOptionalParameter("percIcms", produto.percIcms)
        addOptionalParameter("valorIcms", produto.valorIcms)
        addOptionalParameter("baseIpi", produto.baseIpi)
        addOptionalParameter("percIpi", produto.percIpi)
        addOptionalParameter("valorIpi", produto.valorIpi)
        addOptionalParameter("basePis", produto.basePis)
        addOptionalParameter("percPis", produto.percPis)
        addOptionalParameter("valorPis", produto.valorPis)
        addOptionalParameter("baseCofins", produto.baseCofins)
        addOptionalParameter("percCofins", produto.percCofins)
        addOptionalParameter("valorCofins", produto.valorCofins)
      }
    })
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