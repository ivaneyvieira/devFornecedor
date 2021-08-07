package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.viewmodel.devolucao.IFiltro
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.NONE
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.model.EmailMessage
import br.com.astrosoft.framework.model.GamilFolder.Todos
import br.com.astrosoft.framework.model.MailGMail
import br.com.astrosoft.framework.util.format
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.mail.internet.InternetAddress

class NotaSaida(
  val loja: Int,
  val sigla: String,
  val pdv: Int,
  val transacao: Int,
  val pedido: Int,
  val dataPedido: LocalDate,
  val nota: String,
  val fatura: String,
  val dataNota: LocalDate,
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
               ) {
  fun String.find(regexStr: String): String {
    val regex = regexStr.toRegex()
    val find = regex.find(this)
    val groups = find?.groupValues.orEmpty()
    return groups.getOrNull(1) ?: ""
  }

  var tipoPag: String
    get() {
      val chave = chaveDesconto?.uppercase() ?: ""
      return when {
        chave.startsWith("DESC") -> "Desconto"
        chave.startsWith("DEP")  -> "Deposito"
        else                     -> chaveDesconto?.split(" ")?.getOrNull(0) ?: ""
      }
    }
    set(value) {
      chaveDesconto = "$value | $documentoPag | $niPag | ${vencimentoPag.format()}"
    }

  var documentoPag: String
    get() {
      val chave = chaveDesconto?.uppercase() ?: ""
      return when {
        chave.contains("|") -> chave.split("|").getOrNull(1)?.trim() ?: ""
        else                -> when (tipoPag) {
          "Desconto" -> chave.find(".+NF ([0-9]+).+")
          "Deposito" -> chave.find("^Deposito (.+) [0-9]+\\/[0-9]+\\/[0-9]+$")
          else       -> chave.find(".+NF ([0-9]+).+")
        }
      }
    }
    set(value) {
      chaveDesconto = "$tipoPag | $value | $niPag | ${vencimentoPag.format()}"
    }

  var niPag: String
    get() {
      val chave = chaveDesconto?.uppercase() ?: ""
      return when {
        chave.contains("|") -> chave.split("|").getOrNull(2)?.trim() ?: ""
        else                -> when (tipoPag) {
          "Desconto" -> chave.find(".+NI ([0-9]+).+")
          "Deposito" -> ""
          else       -> chave.find(".+NI ([0-9]+).+")
        }
      }
    }
    set(value) {
      chaveDesconto = "$tipoPag | $documentoPag | $value | ${vencimentoPag.format()}"
    }

  var vencimentoPag: String
    get() {
      val chave = chaveDesconto?.uppercase() ?: ""
      val strData = chave.find("^.+ ([0-9]+\\/[0-9]+\\/[0-9]+)$") ?: ""
      val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy")
      return try {
        LocalDate.parse(strData, dtf).format()
      } catch (e: DateTimeException) {
        val dtf2 = DateTimeFormatter.ofPattern("dd/MM/yy")
        try {
          LocalDate.parse(strData, dtf2).format()
        } catch (e: DateTimeException) {
          ""
        }
      }
    }
    set(value) {
      chaveDesconto = "$tipoPag | $documentoPag | $niPag | $value"
    }

  private var produtos: List<ProdutosNotaSaida>? = null

  fun listaProdutos(): List<ProdutosNotaSaida> {
    if (produtos == null) {
      produtos = when (tipo) {
        "PED" -> saci.produtosPedido(this)
        "ENT" -> saci.produtosEntrada(this)
        "AJT" -> saci.produtosAjuste(this)
        "FIN" -> saci.produtosFinanceiro(this)
        else  -> saci.produtosNotaSaida(this)
      }
    }
    return produtos.orEmpty()
  }

  val valorTotalNota
    get() = icmsSubstProduto + valorFrete + valorSeguro - valorDesconto + valorTotalProduto + outrasDespesas + valorIpiProdutos

  val valorIpiProdutos
    get() = if (tipo == "PED") listaProdutos().sumOf { it.valorIPI } else valorIpi
  val icmsSubstProduto
    get() = if (tipo == "PED") listaProdutos().sumOf { it.vst } else icmsSubst
  val baseIcmsSubstProduto
    get() = if (tipo == "PED") listaProdutos().sumOf { it.baseICMS } else baseIcmsSubst
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

  val valorNota
    get() = if (tipo == "1") valor else listaProdutos().sumOf { it.valorTotalIpi }
  val valorTotalProduto: Double
    get() = listaProdutos().sumOf {
      it.valorTotal
    }

  fun saveRmk() = saci.saveRmk(this)

  fun listFiles() = saci.selectFile(this)

  fun chaveFornecedor() = ChaveFornecedor(custno, fornecedor, vendno, fornecedorSap, email, tipo, rmkVend)

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

  companion object {
    private val fornecedores = mutableListOf<Fornecedor>()

    fun updateNotasDevolucao(filtro: IFiltro) {
      val user = Config.user as? UserSaci
      val loja: Int = if (user?.admin == true) 0 else user?.storeno ?: 0
      val filtroFornecedor = filtro.filtro()

      val notas = when (filtro.serie) {
        PED  -> saci.pedidosDevolucao(filtroFornecedor.loja.no)
        ENT  -> saci.entradaDevolucao()
        AJT  -> saci.ajusteGarantia()
        FIN  -> saci.notaFinanceiro()
        else -> saci.notasDevolucao(filtro.serie)
      }
      val grupos =
              notas.asSequence()
                .filter { it.loja == loja || loja == 0 }
                .filter { filtro.pago66 == NONE || it.serie66Pago == filtro.pago66.value }
                .filter { filtro.pago01 == NONE || it.serie01Pago == filtro.pago01.value }
                .filter { filtro.coleta01 == NONE || it.serie01Coleta == filtro.coleta01.value }
                .filter { filtro.remessaConserto == NONE || it.remessaConserto == filtro.remessaConserto.value }
                .groupBy { it.chaveFornecedor() }
      fornecedores.clear()
      fornecedores.addAll(grupos.map { entry ->
        Fornecedor(entry.key.custno,
                   entry.key.fornecedor,
                   entry.key.vendno,
                   entry.key.fornecedorSap,
                   entry.key.email,
                   entry.key.tipo,
                   entry.key.obs,
                   entry.value)
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