package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie01ColetaViewModel(viewModel: DevolucaoViewModel): TabDevolucaoViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie01Coleta
}

interface ITabNotaSerie01Coleta: ITabNota {
  override val serie: String
    get() = "1"
  override val pago66: String
    get() = ""
  override val coleta01: String
    get() = ""
}