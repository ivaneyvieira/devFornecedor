package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class NotaEntradaNdd(val id: Int,
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
                     val xmlDadosAdicionais: String) {
  val notaFiscal
    get() = "$numero/$serie"
  val fornecedor
    get() = FornecedorNdd(cnpjEmitente, emptyList())
  val storeno
    get() = saci.findLojaByCnpj(cnpjDestinatario)?.no ?: 0
  val labelTitle
    get() = fornecedor.labelTitle
  val dataEmissaoStr
    get() = dataEmissao.format()
}