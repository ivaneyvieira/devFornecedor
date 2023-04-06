package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.nfeXml.ItensNotaReport
import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaEntradaXML(
  val loja: Int,
  val ni: Int,
  val numero: Int,
  val serie: Int,
  val dataEmissao: LocalDate?,
  val dataEntrada: LocalDate?,
  val fornecedorNota: Int?,
  val fornecedorCad: String?,
  val cnpjEmitente: String,
  val nomeFornecedor: String,
  val valorTotalProdutos: Double,
  val valorTotal: Double,
  val chave: String,
  val cfop: Int,
  val cte: Int?,
) {
  val notaFiscal
    get() = "$numero/$serie"

  fun itensNotaReport(): List<ItensNotaReport>? {
    val nota = NotaEntradaFileXML.find(chave) ?: return null
    return nota?.itensNotaReport().orEmpty()
  }

  fun xmlFile(): String? {
    return NotaEntradaFileXML.find(chave)?.xmlFile
  }

  fun xmlCte() = CteXML.findByCte(cte)

  companion object {
    fun findAll(filter: FiltroNotaEntradaXML) = saci.listNFEntrada(filter)
  }
}

data class FiltroNotaEntradaXML(
  val loja: Loja?,
  val dataInicial: LocalDate,
  val dataFinal: LocalDate,
  val numero: Int,
  val cnpj: String,
  val fornecedor: String,
  val query: String,
)