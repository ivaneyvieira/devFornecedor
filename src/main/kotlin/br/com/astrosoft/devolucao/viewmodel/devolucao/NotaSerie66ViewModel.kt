package br.com.astrosoft.devolucao.viewmodel.devolucao

class NotaSerie66ViewModel(viewModel: DevFornecedorViewModel): NotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaDevolucao
}

interface INotaDevolucao: INota {
  override val serie: String
    get() = "66"
}