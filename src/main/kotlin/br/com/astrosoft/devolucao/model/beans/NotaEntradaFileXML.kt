package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class NotaEntradaFileXML(
  val id: Int,
  val numero: Int,
  val serie: Int,
  val cancelado: String,
  val dataEmissao: LocalDate?,
  val cnpjEmitente: String?,
  val nomeFornecedor: String?,
  val cnpjDestinatario: String?,
  val ieEmitente: String?,
  val ieDestinatario: String?,
  val baseCalculoIcms: Double?,
  val baseCalculoSt: Double?,
  val valorTotalProdutos: Double?,
  val valorTotalIcms: Double?,
  val valorTotalSt: Double?,
  val baseCalculoIssqn: Double?,
  val chave: String?,
  val status: String?,
  val xmlAut: String?,
  val xmlCancelado: String?,
  val xmlDadosAdicionais: String?,
  val xmlFile: String?,
                        ) {
  val notaFiscal
    get() = "$numero/$serie"

  val loja = lojas.firstOrNull { it.cnpjLoja == cnpjDestinatario }?.no

  companion object {
    val lojas = Loja.allLojas()
    fun findAll(filter: FiltroNotaEntradaFileXML) = ndd.listNFENtrada(filter)
  }
}

data class FiltroNotaEntradaFileXML(
  val loja: Loja?,
  val dataInicial: LocalDate,
  val dataFinal: LocalDate,
  val numero: Int,
  val cnpj: String,
  val fornecedor: String,
  val query: String,
                                   )