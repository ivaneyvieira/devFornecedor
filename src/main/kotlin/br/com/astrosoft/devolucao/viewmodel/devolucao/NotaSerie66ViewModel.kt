package br.com.astrosoft.devolucao.viewmodel.devolucao

class NotaSerie66ViewModel(viewModel: DevFornecedorViewModel): NotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie66
}

interface INotaSerie66: INota {
  override val serie: String
    get() = "66"
  override val pago66: String
    get() = "N"
}
