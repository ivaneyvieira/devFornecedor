package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.ndd

class CteXML(val xmfFile: String) {
  companion object {
    fun findByCte(cte: Int?) = if (cte == null) null else ndd.listCte(cte)
  }
}