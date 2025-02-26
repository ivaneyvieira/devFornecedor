package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.viewmodel.ITabUser
import br.com.astrosoft.framework.viewmodel.TabUsrViewModel

class TabAvariaRecUsrViewModel(val viewModel: DevolucaoAvariaRecViewModel) : TabUsrViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabAvariaRecUsr

  override fun UserSaci.desative() {
    this.menuDevolucaoAvariaRec = false
  }

  override fun UserSaci.isActive(): Boolean {
    return this.menuDevolucaoAvariaRec
  }

  override fun UserSaci.update(usuario: UserSaci) {
    this.avariaRecEditor = usuario.avariaRecEditor
    this.avariaRecPendente = usuario.avariaRecPendente
    this.avariaRecTransportadora = usuario.avariaRecTransportadora
    this.avariaRecEmail = usuario.avariaRecEmail
    this.avariaRecAcerto = usuario.avariaRecAcerto
    this.avariaRecNFD = usuario.avariaRecNFD
    this.avariaRecReposto = usuario.avariaRecReposto
  }
}

interface ITabAvariaRecUsr : ITabUser
