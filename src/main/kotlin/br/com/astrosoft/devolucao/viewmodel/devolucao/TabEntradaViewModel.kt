package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabEntradaViewModel(viewModel: Devolucao66ViewModel) :
  TabDevolucaoViewModelAbstract<IDevolucao66View>(viewModel) {
  override val subView
    get() = viewModel.view.tabEntrada
}

interface ITabEntrada : ITabNota {
  override val serie: Serie
    get() = Serie.ENT
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}