package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ItensNotaReport
import br.com.astrosoft.devolucao.model.ProdutoNotaEntradaVO
import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.nfeXml.NfeFile
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class NotaEntradaNdd(val id: Int,
                     val storeno: Int,
                     val custno: Int,
                     val nome: String,
                     val codigoSaci: Int,
                     val fornecedorSap: Int,
                     val email: String,
                     val obs: String,
                     val numero: Int,
                     val serie: Int,
                     val dataEmissao: LocalDate?,
                     val cnpjEmitente: String,
                     val cnpjDestinatario: String,
                     val ieEmitente: String,
                     val ieDestinatario: String,
                     val baseCalculoIcms: Double,
                     val baseCalculoSt: Double,
                     val valorTotalProdutos: Double,
                     val valorTotalIcms: Double,
                     val valorTotalSt: Double,
                     val baseCalculoIssqn: Double,
                     val chave: String,
                     val status: String,
                     val xmlAut: String,
                     val xmlCancelado: String,
                     val xmlNfe: String,
                     val xmlDadosAdicionais: String,
                     val notaSaci: String,
                     var ordno: Int,
                     val transportadora: String,
                     val conhecimentoFrete: String) {
  val aliquotaICMSCalculo
    get() = valorTotalIcms / valorTotalProdutos

  fun save() {
    saci.saveNotaNddPedido(this)
  }

  val linhaFatura: String
    get() {
      val notaReport = itensNotaReport().firstOrNull() ?: return ""
      return notaReport.faturaDuplicata
    }

  val valorNota: Double
    get() {
      val notaReport = itensNotaReport().firstOrNull() ?: return 0.00
      return notaReport.vlNota.toDouble()
    }

  fun itensNotaReport(): List<ItensNotaReport> {
    return produtosNfe?.itensNotaReport() ?: emptyList()
  }

  val notaFiscal
    get() = "$numero/$serie"
  val labelTitle
    get() = "FORNECEDOR: ${this.codigoSaci} ${this.nome}"
  val dataEmissaoStr
    get() = dataEmissao.format()
  val nfeFile
    get() = NfeFile(xmlNfe)

  private val produtosNfe: ProdutoNotaEntradaVO? by lazy {
    ndd.produtosNotasEntrada(id)
  }

  val produtosNotaEntradaNDD: List<ProdutoNotaEntradaNdd> by lazy {
    val produtosNfe = produtosNfe ?: return@lazy emptyList()
    produtosNfe.produtosNotaEntradaNDD()
  }

  val temIPI
    get() = produtosNotaEntradaNDD.any {
      it.temIPI
    }
}