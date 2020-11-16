package br.com.astrosoft.devolucao.viewmodel.devolucao

class NotaSerie01ViewModel(viewModel: DevFornecedorViewModel): NotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaVenda
}

interface INotaVenda: INota {
  override val serie: String
    get() = "1"
}