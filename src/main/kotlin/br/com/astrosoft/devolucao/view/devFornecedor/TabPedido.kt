package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.viewmodel.devolucao.IPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.PedidoViewModel

class TabPedido(viewModel: PedidoViewModel): TabFornecedorAbstract(viewModel), IPedido {
  override val label: String
    get() = "Pedido"
  
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.pedido == true
  }
}