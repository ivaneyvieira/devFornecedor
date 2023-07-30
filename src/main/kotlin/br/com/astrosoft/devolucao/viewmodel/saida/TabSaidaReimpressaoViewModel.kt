package br.com.astrosoft.devolucao.viewmodel.saida

import br.com.astrosoft.devolucao.model.beans.FiltroReimpressao
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.ReimpressaoNota
import br.com.astrosoft.framework.viewmodel.ITabView

class TabSaidaReimpressaoViewModel(val viewModel: SaidaViewModel) {
  val subView
    get() = viewModel.view.tabSaidaReimpressaoViewModel

  fun updateView() {
    val filtro = subView.filtro()
    val resultList = ReimpressaoNota.findReimpressao(filtro).toList()
    subView.updateGrid(resultList)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }.toList()
  }
}

interface ITabSaidaReimpressaoViewModel : ITabView {
  fun filtro(): FiltroReimpressao
  fun updateGrid(itens: List<ReimpressaoNota>)
  fun confirmaRemocao(exec: () -> Unit)
}