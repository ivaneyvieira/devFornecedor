package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItem
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImposto
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemProduto
import com.fincatto.documentofiscal.utils.DFPersister

class ProdutoNotaEntradaVO(val id: Int, val xmlNfe: String) {
  val produtosNotaEntradaNDD: List<ProdutoNotaEntradaNdd> by lazy {
    val nota: NFNota = DFPersister(false).read(NFNota::class.java, xmlNfe)
    val produtosNota = nota.info?.itens ?: emptyList()
    produtosNota.mapNotNull(::mapProduto)
  }

  private fun mapProduto(item: NFNotaInfoItem): ProdutoNotaEntradaNdd {
    val produto: NFNotaInfoItemProduto? = item.produto
    val imposto: NFNotaInfoItemImposto? = item.imposto
    return ProdutoNotaEntradaNdd(codigo = produto?.codigo ?: "",
                                 codBarra = produto?.codigoDeBarras ?: "",
                                 descricao = produto?.descricao ?: "",
                                 ncm = produto?.ncm ?: "",
                                 cst = produto?.codigoEspecificadorSituacaoTributaria ?: "",
                                 cfop = produto?.cfop ?: "",
                                 un = produto?.unidadeComercial ?: "",
                                 quantidade = produto?.quantidadeComercial?.toDoubleOrNull() ?: 0.00,
                                 valorUnitario = produto?.valorUnitario?.toDoubleOrNull() ?: 0.00,
                                 valorTotal = produto?.valorTotalBruto?.toDoubleOrNull() ?: 0.00,
                                 baseICMS = imposto?.icms?.icms00?.valorBaseCalculo?.toDoubleOrNull() ?: 0.00,
                                 valorIPI = imposto?.ipi?.tributado?.valorTributo?.toDoubleOrNull() ?: 0.00,
                                 aliqICMS = imposto?.icms?.icms00?.percentualAliquota?.toDoubleOrNull() ?: 0.00,
                                 aliqIPI = imposto?.ipi?.tributado?.percentualAliquota?.toDoubleOrNull() ?: 0.00)
  }
}