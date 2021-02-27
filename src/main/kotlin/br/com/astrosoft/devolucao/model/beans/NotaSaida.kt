package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.ENT
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.PED
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.NONE
import br.com.astrosoft.framework.model.EmailMessage
import br.com.astrosoft.framework.model.GamilFolder.Todos
import br.com.astrosoft.framework.model.MailGMail
import java.time.LocalDate
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
  val chave: String
               ) {
  fun listaProdutos() = when (tipo) {
    "PED" -> saci.produtosPedido(this)
    "ENT" -> saci.produtosEntrada(this)
    else  -> saci.produtosNotaSaida(this)
  }

  val valorNota
    get() = if (tipo == "1") valor else listaProdutos().sumByDouble { it.valorTotalIpi }
  val valorTotalProduto: Double
    get() = listaProdutos().sumByDouble {
      it.valorTotal
    }

  fun saveRmk() = saci.saveRmk(this)

  fun listFiles() = saci.selectFile(this)

  fun chaveFornecedor() = ChaveFornecedor(custno, fornecedor, vendno, email, tipo, rmkVend)

  fun salvaEmail(gmail: EmailGmail, idEmail: Int) {
    saci.salvaEmailEnviado(gmail, this, idEmail)
  }

  fun listEmailNota() = saci.listEmailNota(this)

  fun listaEmailRecebidoNota(): List<EmailDB> {
    val gmail = MailGMail()
    val numero = if (tipo == "PED") pedido.toString() else nota.split("/")[0]
    return gmail.listEmail(Todos, numero).map { msg: EmailMessage ->
      EmailDB(
        storeno = loja,
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
        anexos = "N"
             )
    }
  }

  companion object {
    private val fornecedores = mutableListOf<Fornecedor>()

    fun updateNotasDevolucao(
      serie: Serie, pago66: SimNao, coleta66: SimNao, remessaConserto: SimNao
                            ) {
      val user = AppConfig.user as? UserSaci
      val loja = if (user?.admin == true) 0 else user?.storeno ?: 0
      val notas = when (serie) {
        PED  -> saci.pedidosDevolucao()
        ENT  -> saci.entradaDevolucao()
        else -> saci.notasDevolucao(serie)
      }
      val grupos = notas.asSequence()
        .filter { it.loja == loja || loja == 0 }
        .filter { pago66 == NONE || it.serie66Pago == pago66.value }
        .filter { coleta66 == NONE || it.serie01Coleta == coleta66.value }
        .filter { remessaConserto == NONE || it.remessaConserto == remessaConserto.value }
        .groupBy { it.chaveFornecedor() }
      fornecedores.clear()
      fornecedores.addAll(grupos.map { entry ->
        Fornecedor(
          entry.key.custno,
          entry.key.fornecedor,
          entry.key.vendno,
          entry.key.email,
          entry.key.tipo,
          entry.key.obs,
          entry.value
                  )
      })
    }

    fun findFornecedores() = fornecedores.toList()
  }
}

data class ChaveFornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val email: String,
  val tipo: String,
  val obs: String
                          )