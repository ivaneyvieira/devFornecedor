package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.FiltroNfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.model.gridlazy.IServiceQuery
import br.com.astrosoft.framework.model.gridlazy.SortOrder

class UltimaNotaEntradaServiceQuery : IServiceQuery<NfPrecEntrada, FiltroNfPrecEntrada> {
  override fun count(filter: FiltroNfPrecEntrada): Int {
    return saci.countNfPrec(filter)
  }

  override fun fetch(filter: FiltroNfPrecEntrada,
                     offset: Int,
                     limit: Int,
                     sortOrders: List<SortOrder>): List<NfPrecEntrada> {
    return saci.fetchNfPrec(filter, offset, limit, sortOrders)
  }
}