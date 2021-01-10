package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie66PagoViewModel(viewModel: DevolucaoViewModel): TabDevolucaoViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie66Pago
}

interface ITabNotaSerie66Pago: ITabNota {
  override val serie: String
    get() = "66"
  override val pago66: String
    get() = "S"
}