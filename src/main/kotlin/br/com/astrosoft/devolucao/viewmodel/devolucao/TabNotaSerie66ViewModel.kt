package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie66ViewModel(viewModel: DevFornecedorViewModel): AbstractNotaSerieViewModel(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie66
}

interface ITabNotaSerie66: ITabNota {
  override val serie: String
    get() = "66"
  override val pago66: String
    get() = "N"
}
