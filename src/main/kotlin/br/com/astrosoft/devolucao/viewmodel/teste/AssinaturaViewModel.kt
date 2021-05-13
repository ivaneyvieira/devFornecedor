package br.com.astrosoft.devolucao.viewmodel.teste

import br.com.astrosoft.devolucao.viewmodel.agenda.IAgendaView
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class AssinaturaViewModel(view: IAssinaturaView) : ViewModel<IAssinaturaView>(view) {
  override fun listTab()  = emptyList<ITabView>()
}

interface IAssinaturaView : IView