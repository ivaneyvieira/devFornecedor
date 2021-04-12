package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie01PagoViewModel(viewModel: DevolucaoViewModel) : TabDevolucaoViewModelAbstract(
  viewModel
                                                                                            ) {
  override val subView
    get() = viewModel.view.tabNotaSerie01
}

interface ITabNotaSerie01Pago : ITabNota {
  override val serie: Serie
    get() = Serie.Serie01
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.SIM
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}