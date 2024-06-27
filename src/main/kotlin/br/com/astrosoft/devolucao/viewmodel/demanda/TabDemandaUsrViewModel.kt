package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.viewmodel.ITabUser
import br.com.astrosoft.framework.viewmodel.TabUsrViewModel

class TabDemandaUsrViewModel(val viewModel: DemandaViewModel) : TabUsrViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabDemandaUsr

  override fun UserSaci.desative() {
    this.menuDemanda = false
  }

  override fun UserSaci.isActive(): Boolean {
    return this.menuDemanda
  }

  override fun UserSaci.update(usuario: UserSaci) {
    this.demandaConcluido = usuario.demandaConcluido
    this.demandaAgenda = usuario.demandaAgenda
    this.pedidoFornecedor = usuario.pedidoFornecedor
  }
}

interface ITabDemandaUsr : ITabUser
