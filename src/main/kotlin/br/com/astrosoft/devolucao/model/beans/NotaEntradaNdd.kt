package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.nfeXml.NfeFile
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class NotaEntradaNdd(val id: Int,
                     val storeno: Int,
                     val custno: Int,
                     val nome: String,
                     val vendno: Int,
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
                     val notaSaci: String) {
  val notaFiscal
    get() = "$numero/$serie"
  val labelTitle
    get() = "FORNECEDOR: ${this.vendno} ${this.nome}"
  val dataEmissaoStr
    get() = dataEmissao.format()
  val nfeFile
    get() = NfeFile(xmlNfe)
}