package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DemandaViewModel(view: IDemandaView) : ViewModel<IDemandaView>(view) {
  val tabAgendadaDemanda = TabAgendaDemandaViewModel(this)
  val tabConcluidoDemanda = TabConcluidoDemandaViewModel(this)
  val tabFornecedorDemanda = TabFornecedorDemandaViewModel(this)
  val tabContaRazaoDemanda = TabContaRazaoDemandaViewModel(this)
  val tabDemandaUsr = TabDemandaUsrViewModel(this)

  override fun listTab(): List<ITabView> {
    return listOf(
      view.tabAgendaDemanda,
      view.tabConcluidoDemanda,
      view.tabFornecedorDemanda,
      view.tabContaRazaoDemanda,
      view.tabDemandaUsr
    )
  }
}

interface IDemandaView : IView {
  val tabAgendaDemanda: ITabAgendaDemanda
  val tabConcluidoDemanda: ITabConcluidoDemanda
  val tabFornecedorDemanda: ITabFornecedorDemanda
  val tabContaRazaoDemanda: ITabContaRazaoDemanda
  val tabDemandaUsr: ITabDemandaUsr
}