package br.com.astrosoft.devolucao.viewmodel.saida

import br.com.astrosoft.devolucao.model.beans.FiltroNotaSaidaNdd
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaSaidaNdd
import br.com.astrosoft.devolucao.model.reports.DanfeReport
import br.com.astrosoft.framework.viewmodel.ITabView

class TabSaidaNddViewModel(val viewModel: SaidaViewModel) {
  val subView
    get() = viewModel.view.tabSaidaNddViewModel

  fun updateView() {
    val filtro = subView.filtro()
    val resultList = NotaSaidaNdd.findAll(filtro)
    subView.updateGrid(resultList)
  }

  fun createDanfe(nota: NotaSaidaNdd) {
    val itensNotaReport = nota.itensNotaReport()
    val report = DanfeReport.create(itensNotaReport)
    viewModel.view.showReport("Danfee", report)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }
}

interface ITabSaidaNddViewModel : ITabView {
  fun filtro(): FiltroNotaSaidaNdd
  fun updateGrid(itens: List<NotaSaidaNdd>)
}