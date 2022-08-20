package br.com.astrosoft.devolucao.viewmodel.saida

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.reports.DanfeReport
import br.com.astrosoft.devolucao.model.reports.ETIPO_COPIA
import br.com.astrosoft.framework.viewmodel.ITabView

class TabSaidaReimpressaoViewModel(val viewModel: SaidaViewModel) {
  val subView
    get() = viewModel.view.tabSaidaReimpressaoViewModel

  fun updateView() {
    val filtro = subView.filtro()
    val resultList = ReimpressaoNota.findReimpressao(filtro)
    subView.updateGrid(resultList)
  }


  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }
}

interface ITabSaidaReimpressaoViewModel : ITabView {
  fun filtro(): FiltroReimpressao
  fun updateGrid(itens: List<ReimpressaoNota>)
}