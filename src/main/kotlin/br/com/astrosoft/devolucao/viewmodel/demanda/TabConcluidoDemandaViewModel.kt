package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.FilterAgendaDemanda
import br.com.astrosoft.framework.viewmodel.ITabView

class TabConcluidoDemandaViewModel(val viewModel: DemandaViewModel) {
  fun updateView() {
    val filter = subView.filter()
    val list = AgendaDemanda.findAll(filter).toList()
    subView.updateGrid(list)
  }

  fun anexo(demanda: AgendaDemanda) = viewModel.exec {
    subView.showAnexoForm(demanda)
  }

  fun visualizar(demanda: AgendaDemanda) {
    subView.showForm(demanda)
  }

  fun voltaDemanda() {
    val list = subView.selectedItem()
    list.forEach {
      it.marcaVolta()
    }
    updateView()
  }

  val subView
    get() = viewModel.view.tabConcluidoDemanda
}

interface ITabConcluidoDemanda : ITabView {
  fun updateGrid(itens: List<AgendaDemanda>)

  fun showAnexoForm(demanda: AgendaDemanda)
  fun showForm(demanda: AgendaDemanda)
  fun filter(): FilterAgendaDemanda
  fun selectedItem(): List<AgendaDemanda>
}