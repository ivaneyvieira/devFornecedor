package br.com.astrosoft.devolucao.viewmodel.devolucao

class PedidoViewModel(viewModel: DevFornecedorViewModel): NotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabPedido
}

interface IPedido : INota {
  override val serie: String
    get() = "PED"
}