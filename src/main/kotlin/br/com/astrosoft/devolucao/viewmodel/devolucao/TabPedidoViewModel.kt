package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabPedidoViewModel(viewModel: DevolucaoViewModel): TabDevolucaoViewModelAbstract(viewModel) {
  override val subView
    get() = viewModel.view.tabPedido
}

interface ITabPedido: ITabNota {
  override val serie: String
    get() = "PED"
  override val pago66: String
    get() = ""
  override val coleta01: String
    get() = ""
}