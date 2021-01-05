package br.com.astrosoft.devolucao.viewmodel.devolucao

class NotaSerie01ViewModel(viewModel: DevFornecedorViewModel): AbstractNotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie01
}

interface INotaSerie01: INota {
  override val serie: String
    get() = "1"
  override val pago66: String
    get() = ""
}