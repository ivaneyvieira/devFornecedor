package br.com.astrosoft.devolucao.model

import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.framework.model.gridlazy.IServiceQuery
import br.com.astrosoft.framework.model.gridlazy.SortOrder

class UltimaNotaEntradaServiceQuery : IServiceQuery<UltimaNotaEntrada, FiltroUltimaNotaEntrada> {
  override fun count(filter: FiltroUltimaNotaEntrada): Int {
    return saci.countUltimaNota(filter)
  }

  override fun fetch(filter: FiltroUltimaNotaEntrada,
                     offset: Int,
                     limit: Int,
                     sortOrders: List<SortOrder>): List<UltimaNotaEntrada> {
    return saci.fetchUltimaNota(filter, offset, limit, sortOrders)
  }
}