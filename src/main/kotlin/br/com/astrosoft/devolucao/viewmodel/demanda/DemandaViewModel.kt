package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DemandaViewModel(view: IDemandaView) : ViewModel<IDemandaView>(view) {
  val tabAgendadaDemanda = TabAgendaDemandaViewModel(this)

  override fun listTab(): List<ITabView> {
    return listOf(view.tabAgendaDemanda)
  }
}

interface IDemandaView : IView {
  val tabAgendaDemanda: ITabAgendaDemanda
}