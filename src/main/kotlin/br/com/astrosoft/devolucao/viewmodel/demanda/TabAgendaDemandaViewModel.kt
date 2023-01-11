package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.framework.viewmodel.ITabView

class TabAgendaDemandaViewModel(val viewModel: DemandaViewModel) {
  val subView
    get() = viewModel.view.tabAgendaDemanda
}

interface ITabAgendaDemanda : ITabView {

}