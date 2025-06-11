package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class ProdutoKit {
  var prdnoK: String? = null
  var prdno: String? = null
  var descricao: String? = null
  var quant: Int? = null
  var custo: Double? = null

  companion object {
    fun find(prdno: String): List<ProdutoKit> {
      return saci.findProdutoKit(prdno)
    }
  }
}