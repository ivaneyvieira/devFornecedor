package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie66PagoViewModel(viewModel: Devolucao66ViewModel) :
    TabDevolucaoViewModelAbstract<IDevolucao66View>(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie66Pago
}

interface ITabNotaSerie66Pago : ITabNota {
  override val serie: Serie
    get() = Serie.Serie66
  override val pago66: SimNao
    get() = SimNao.SIM
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}