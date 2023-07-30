package br.com.astrosoft.devolucao.viewmodel.saida

import br.com.astrosoft.devolucao.model.beans.FiltroNotaSaidaNdd
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaSaidaNdd
import br.com.astrosoft.framework.viewmodel.ITabView

class TabSaidaNddViewModel(val viewModel: SaidaViewModel) {
  val subView
    get() = viewModel.view.tabSaidaNddViewModel

  fun updateView() {
    val filtro = subView.filtro()
    if (filtro.isEmpty()) {
      limparView()
    } else {
      val resultList = NotaSaidaNdd.findAll(subView.filtro()).toList()
      subView.limparFiltro()
      subView.updateGrid(resultList)
    }
  }

  fun limparView() {
    subView.limparFiltro()
    subView.updateGrid(emptyList())
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }.toList()
  }
}

interface ITabSaidaNddViewModel : ITabView {
  fun filtro(): FiltroNotaSaidaNdd
  fun updateGrid(itens: List<NotaSaidaNdd>)
  fun limparFiltro()
  fun setDateNow()
}