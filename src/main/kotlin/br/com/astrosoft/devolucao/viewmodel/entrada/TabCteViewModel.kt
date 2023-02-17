package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.viewmodel.ITabView

class TabCteViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabCteViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryCte(subView.getFiltro())
    subView.openRelatorio()
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(): List<NfEntradaFrete> {
    return NfEntradaFrete.findNotas()
  }

  fun findTabName(carrno : Int?, codTab: Int?): String {
    codTab ?: return ""
    carrno ?: return ""
    return saci.findTabName(carrno  ,  codTab)?.nome ?: "Não Encontrada"
  }
}

interface ITabCteViewModel : ITabView {
  fun setFiltro(filtro: FiltroNFEntradaFrete)
  fun getFiltro(): FiltroNFEntradaFrete
  fun openRelatorio()
}