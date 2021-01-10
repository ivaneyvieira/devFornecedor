package br.com.astrosoft.devolucao.viewmodel

import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DefautlViewModel(view: IDefaultView): ViewModel<IDefaultView>(view) {
  override fun listTab(): List<ITabView> = emptyList()
}

interface IDefaultView: IView
