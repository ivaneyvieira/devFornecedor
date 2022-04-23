package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IFiltro
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.NONE
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.model.EmailMessage
import br.com.astrosoft.framework.model.GamilFolder.Todos
import br.com.astrosoft.framework.model.MailGMail
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.localDate
import br.com.astrosoft.framework.util.toSaciDate
import org.apache.commons.lang3.StringUtils
import java.time.LocalDate
import java.util.*
import javax.mail.internet.InternetAddress
import kotlin.math.round

class NotaSaida(val loja: Int,
                val sigla: String,
                val pdv: Int,
                val transacao: Int,
                val pedido: Int,
                val dataPedido: LocalDate?,
                val nota: String,
                val fatura: String,
                val dataNota: LocalDate?,
                val custno: Int,
                val fornecedor: String,
                val email: String,
                val vendno: Int,
                val fornecedorSap: Int,
                var rmk: String,
                val valor: Double,
                val obsNota: String,
                val serie01Rejeitada: String,
                val serie01Pago: String,
                val serie01Coleta: String,
                val serie66Pago: String,
                val remessaConserto: String,
                val remarks: String,
                val baseIcms: Double = 0.00,
                val valorIcms: Double = 0.00,
                val baseIcmsSubst: Double = 0.00,
                val icmsSubst: Double = 0.00,
                val valorFrete: Double = 0.00,
                val valorSeguro: Double = 0.00,
                val valorDesconto: Double = 0.00,
                val outrasDespesas: Double = 0.00,
                val valorIpi: Double = 0.00,
                val valorTotal: Double = 0.00,
                val obsPedido: String,
                val tipo: String,
                val rmkVend: String,
                val chave: String,
                val natureza: String,
                var chaveDesconto: String?,
                var observacaoAuxiliar: String?,
                var dataAgenda: LocalDate?,
                val nfAjuste: String?) {
  var dataSituacao: LocalDate?
    get() {
      val dataSaci = observacaoAuxiliar?.split(":")?.getOrNull(0)?.toIntOrNull()
      return dataSaci?.localDate()
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = value?.toSaciDate()?.toString() ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var situacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(1) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = value
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var usuarioSituacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(2) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = value
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var tituloSituacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(3) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = value
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var niSituacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(4) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = value
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var docSituacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(5) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = value
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var notaSituacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(6) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = value
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var valorSituacao: Double?
    get() {
      val valorInt = observacaoAuxiliar?.split(":")?.getOrNull(7)?.toIntOrNull()
      return if (valorInt == null) null else valorInt * 1.00 / 100.00
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = if (value == null) "" else round(value * 100.00).toInt().toString()
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var pedidoEditavel: Int?
    get() {
      val valorInt = observacaoAuxiliar?.split(":")?.getOrNull(8)?.toIntOrNull()
      return valorInt
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = value?.toString() ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var niBonificacao: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(9) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = value
      val niVal = split?.getOrNull(10) ?: ""
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }
  var niValor: String
    get() {
      return observacaoAuxiliar?.split(":")?.getOrNull(10) ?: ""
    }
    set(value) {
      val split = observacaoAuxiliar?.split(":")
      val data = split?.getOrNull(0) ?: ""
      val situacao = split?.getOrNull(1) ?: ""
      val usuario = split?.getOrNull(2) ?: ""
      val titulo = split?.getOrNull(3) ?: ""
      val ni = split?.getOrNull(4) ?: ""
      val doc = split?.getOrNull(5) ?: ""
      val nota = split?.getOrNull(6) ?: ""
      val valor = split?.getOrNull(7) ?: ""
      val pedido = split?.getOrNull(8) ?: ""
      val niBonif = split?.getOrNull(9) ?: ""
      val niVal = value
      observacaoAuxiliar = "$data:$situacao:$usuario:$titulo:$ni:$doc:$nota:$valor:$pedido:$niBonif:$niVal"
    }

  val situacaoStr: String
    get() {
      return if (situacao == "") ""
      else ESituacaoPendencia.values().firstOrNull { sit ->
        sit.valueStr == situacao
      }?.descricao ?: ESituacaoPedido.values().firstOrNull { sit ->
        sit.valueStr == situacao
      }?.descricao ?: ""
    }

  val situacaoPendencia
    get() = ESituacaoPendencia.values().firstOrNull { it.valueStr == situacao }

  private var produtos: List<ProdutosNotaSaida>? = null

  fun listaProdutos(): List<ProdutosNotaSaida> {
    if (produtos == null) {
      produtos = when (tipo) {
        "PED" -> saci.produtosPedido(this)
        "ENT" -> saci.produtosEntrada(this)
        "AJT" -> saci.produtosAjuste(this)
        "AJP" -> saci.produtosAjuste(this)
        "FIN" -> saci.produtosFinanceiro(this)
        else  -> saci.produtosNotaSaida(this)
      }
    }
    return produtos.orEmpty()
  }

  fun isObservacaoFinanceiro(): Boolean {
    val chave = this.chaveDesconto?.uppercase(Locale.getDefault()) ?: return false
    val chaveMaiuscula = StringUtils.stripAccents(chave).uppercase()
    return chaveMaiuscula.contains("CREDITO NA CONTA") || chaveMaiuscula.contains("DESCONTO NA NOTA") || chaveMaiuscula.contains(
      "DESCONTO NO TITULO") || chaveMaiuscula.contains("REPOSICAO") || chaveMaiuscula.contains("RETORNO") || chaveMaiuscula.contains(
      "DESC TITULO") || chaveMaiuscula.contains("CREDITO CONTA") || chaveMaiuscula.contains("CREDITO TITULO") || chaveMaiuscula.contains(
      "CREDITO APLICADO") || chaveMaiuscula.contains("CREDITO DUP") || chaveMaiuscula.contains("BONIFICADA")
  }

  val valorTotalNota
    get() = icmsSubstProduto + valorFrete + valorSeguro - valorDesconto + valorTotalProduto + outrasDespesas + valorIpiProdutos

  val valorIpiProdutos
    get() = if (tipo == "PED") listaProdutos().sumOf { it.valorIPI } else valorIpi
  val icmsSubstProduto
    get() = if (tipo == "PED") listaProdutos().sumOf { it.vst } else icmsSubst
  val baseIcmsSubstProduto
    get() = if (tipo == "PED") listaProdutos().sumOf { it.baseSt } else baseIcmsSubst
  val valorIcmsProdutos
    get() = if (tipo == "PED") listaProdutos().sumOf { it.valorICMS } else valorIcms
  val baseIcmsProdutos
    get() = if (tipo == "PED") listaProdutos().sumOf { it.baseICMS } else baseIcms
  val dataNotaStr
    get() = (if (tipo == "PED") dataPedido else dataNota).format()
  val numeroNotaPedido
    get() = if (tipo == "PED") pedido.toString() else nota

  val labelTitle
    get() = "DEV FORNECEDOR: ${this.custno} ${this.fornecedor} (${this.vendno}) FOR SAP ${this.fornecedorSap}"
  val labelTitle2
    get() = "Fornecedor: ${this.vendno} / ${this.custno} - ${this.fornecedor} (SAP ${this.fornecedorSap})"
  val labelTitlePedido
    get() = "FORNECEDOR: ${this.custno} ${this.fornecedor} (${this.vendno}) FOR SAP ${this.fornecedorSap}"

  val valorNota
    get() = when (tipo) {
      "1"  -> valor
      else -> listaProdutos().sumOf { it.valorTotalIpi }
    }
  val valorTotalProduto: Double
    get() = listaProdutos().sumOf {
      it.valorTotal
    }

  fun saveRmk() = saci.saveRmk(this)

  fun listFiles() = saci.selectFile(this)

  fun chaveFornecedor() = ChaveFornecedor(custno = custno,
                                          fornecedor = fornecedor,
                                          vendno = vendno,
                                          fornecedorSap = fornecedorSap,
                                          email = email,
                                          tipo = tipo,
                                          obs = rmkVend)

  fun salvaEmail(gmail: EmailGmail, idEmail: Int) {
    saci.salvaEmailEnviado(gmail, this, idEmail)
  }

  fun listEmailNota() = saci.listEmailNota(this)

  fun listaEmailRecebidoNota(): List<EmailDB> {
    val gmail = MailGMail()
    val numero = if (tipo == "PED") pedido.toString() else nota.split("/")[0]
    return gmail.listEmail(Todos, numero).map { msg: EmailMessage ->
      EmailDB(storeno = loja,
              pdvno = pdv,
              xano = transacao,
              data = msg.data.toLocalDate(),
              hora = msg.data.toLocalTime(),
              idEmail = 0,
              messageID = msg.messageID,
              email = (msg.from.getOrNull(0) as? InternetAddress)?.address ?: "",
              assunto = msg.subject,
              msg = msg.content().messageTxt,
              planilha = "N",
              relatorio = "N",
              anexos = "N")
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as NotaSaida

    if (loja != other.loja) return false
    if (pdv != other.pdv) return false
    if (transacao != other.transacao) return false

    return true
  }

  override fun hashCode(): Int {
    var result = loja
    result = 31 * result + pdv
    result = 31 * result + transacao
    return result
  }

  private var notaOrigem: NotaEntradaNdd? = null

  fun findNotaOrigem(): NotaEntradaNdd? {
    notaOrigem = null
    val notaSpt = nota.split("/")
    val numero = notaSpt.getOrNull(0)?.toIntOrNull() ?: return null
    val serie = notaSpt.getOrNull(1)?.toIntOrNull() ?: return null
    val notaSaida = ndd.produtosNotasSaida(storeno = loja, numero = numero, serie = serie) ?: return null
    val chaveRef = notaSaida.refNFe ?: return null
    notaOrigem = FornecedorNdd.findNota(chaveRef)
    return notaOrigem
  }

  fun notaOrigem(): NotaEntradaNdd? {
    return notaOrigem ?: findNotaOrigem()
  }

  val transportadora: String
    get() = notaOrigem?.transportadora ?: ""
  val conhecimentoFrete: String
    get() = notaOrigem?.conhecimentoFrete ?: ""
  val dataNfOrigemStr: String
    get() = notaOrigem?.dataEmissaoStr ?: ""
  val nfOrigem: String
    get() = notaOrigem?.notaFiscal ?: ""
  val dataCteStr: String
    get() = ""
  val obsOrigem: String
    get() = notaOrigem?.obs ?: ""

  companion object {
    private val fornecedores = mutableListOf<Fornecedor>()

    fun updateNotasDevolucao(filtro: IFiltro) {
      val user = Config.user as? UserSaci
      val loja: Int = if (user?.admin == true) 0 else user?.storeno ?: 0
      val filtroFornecedor = filtro.filtro()

      val notas = when (filtro.serie) {
        PED  -> saci.pedidosDevolucao(filtroFornecedor.loja.no)
        ENT  -> saci.entradaDevolucao()
        AJT  -> saci.ajusteGarantia(filtro.serie)
        AJD  -> saci.ajusteGarantia(filtro.serie)
        AJP  -> saci.ajusteGarantia(filtro.serie)
        AJC  -> saci.ajusteGarantia(filtro.serie)
        A66  -> saci.ajusteGarantia66(AJT)
        FIN  -> saci.notaFinanceiro()
        else -> saci.notasDevolucao(filtro.serie)
      }.filter { nota ->
        val situacaoPendencia = filtro.situacaoPendencia?.valueStr
        if (situacaoPendencia == null) {
          val situacaoPedido = filtro.situacaoPedido.map { it.valueStr }
          if (situacaoPedido.isEmpty()) {
            true
          }
          else {
            nota.situacao in situacaoPedido
          }
        }
        else {
          nota.situacao == situacaoPendencia
        }
      }
      val grupos =
        notas
          .asSequence()
          .filter { it.loja == loja || loja == 0 }
          .filter { filtro.pago66 == NONE || it.serie66Pago == filtro.pago66.value }
          .filter { filtro.pago01 == NONE || it.serie01Pago == filtro.pago01.value }
          .filter { filtro.coleta01 == NONE || it.serie01Coleta == filtro.coleta01.value }
          .filter { filtro.remessaConserto == NONE || it.remessaConserto == filtro.remessaConserto.value }
          .groupBy { it.chaveFornecedor() }
      fornecedores.clear()
      fornecedores.addAll(grupos.map { entry ->
        Fornecedor(custno = entry.key.custno,
                   fornecedor = entry.key.fornecedor,
                   vendno = entry.key.vendno,
                   fornecedorSap = entry.key.fornecedorSap,
                   email = entry.key.email,
                   tipo = entry.key.tipo,
                   obs = entry.key.obs,
                   notas = entry.value)
      })
    }

    fun findFornecedores(txt: String) = fornecedores.toList().filtro(txt)

    private fun List<Fornecedor>.filtro(txt: String): List<Fornecedor> {
      return this.filter {
        val txtFiltro = txt.trim()
        if (txtFiltro == "") true
        else {
          val filtroNum = txtFiltro.toIntOrNull() ?: 0
          it.custno == filtroNum || it.vendno == filtroNum || it.fornecedor.startsWith(txtFiltro, ignoreCase = true)
        }
      }
    }

    fun salvaDesconto(notaSaida: NotaSaida) {
      saci.salvaDesconto(notaSaida)
    }
  }
}

data class ChaveFornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val fornecedorSap: Int,
  val email: String,
  val tipo: String,
  val obs: String,
                          )