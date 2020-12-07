package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.viewmodel.devolucao.IPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.PedidoViewModel

class TabPedido(viewModel: PedidoViewModel): TabFornecedorAbstract(viewModel), IPedido {
  override val label: String
    get() = "Pedido"
}