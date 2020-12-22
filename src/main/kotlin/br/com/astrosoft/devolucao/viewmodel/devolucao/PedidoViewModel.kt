package br.com.astrosoft.devolucao.viewmodel.devolucao

class PedidoViewModel(viewModel: DevFornecedorViewModel): AbstractNotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabPedido
}

interface IPedido : INota {
  override val serie: String
    get() = "PED"
  override val pago66: String
    get() = ""
}