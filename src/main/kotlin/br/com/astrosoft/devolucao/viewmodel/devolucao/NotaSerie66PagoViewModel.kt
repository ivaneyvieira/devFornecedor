package br.com.astrosoft.devolucao.viewmodel.devolucao

class NotaSerie66PagoViewModel(viewModel: DevFornecedorViewModel): NotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabNota66Pago
}


interface INota66Pago: INota {
  override val serie: String
    get() = "66"
  override val pago66: String
    get() = "S"
}