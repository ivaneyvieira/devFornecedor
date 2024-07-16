package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.viewmodel.ITabUser
import br.com.astrosoft.framework.viewmodel.TabUsrViewModel

class TabPedidoUsrViewModel(val viewModel: DevolucaoPedidoViewModel) : TabUsrViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabPedidoUsr

  override fun UserSaci.desative() {
    this.menuDevolucaoPedido = false
  }

  override fun UserSaci.isActive(): Boolean {
    return this.menuDevolucaoPedido
  }

  override fun UserSaci.update(usuario: UserSaci) {
    this.pedidoEditor = usuario.pedidoEditor
    this.pedidoPendente = usuario.pedidoPendente
    this.pedidoFinalizado = usuario.pedidoFinalizado
    this.pedidoMonitoramentoEntrada = usuario.pedidoMonitoramentoEntrada
  }
}

interface ITabPedidoUsr : ITabUser
