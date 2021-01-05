package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoViewModel

class TabPedido(viewModel: TabPedidoViewModel): TabFornecedorAbstract(viewModel), ITabPedido {
  override val label: String
    get() = "Pedido"
  
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.pedido == true
  }
}