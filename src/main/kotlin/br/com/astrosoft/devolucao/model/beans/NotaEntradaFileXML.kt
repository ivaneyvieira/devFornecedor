package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd
import br.com.astrosoft.devolucao.model.nfeXml.ItensNotaReport
import br.com.astrosoft.devolucao.model.nfeXml.ProdutoNotaEntradaVO
import br.com.astrosoft.framework.util.format
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota
import com.fincatto.documentofiscal.utils.DFPersister
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
  val dataHoraRecebimento: String?,
  val numeroProtocolo: String?,
                        ) {
  val notaFiscal
    get() = "$numero/$serie"

  val loja = lojas.firstOrNull { it.cnpjLoja == cnpjDestinatario }?.no

  fun itensNotaReport(): List<ItensNotaReport> {
    return try {
      xmlFile ?: return emptyList()
      println("XML FILE: $xmlFile")
      val nota = DFPersister(false).read(NFNota::class.java, xmlFile) ?: return emptyList()
      val data = dataHoraRecebimento?.split("T")?.getOrNull(0) ?: ""
      val hora = dataHoraRecebimento?.split("T")?.getOrNull(1)?.split("-")?.getOrNull(0) ?: ""
      val dataFormat = data.substring(8, 10) + "/" + data.substring(5, 7) + "/" + data.substring(0, 4)
      ProdutoNotaEntradaVO.mapReport(nota, "$numeroProtocolo $dataFormat $hora")
    } catch (e: Throwable) {
      e.printStackTrace()
      emptyList()
    }
  }

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