package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaColetaViewModel(viewModel: Devolucao01ViewModel) :
  TabDevolucaoViewModelAbstract<IDevolucao01View>(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaColeta
}

interface ITabNotaColeta : ITabNota {
  override val serie: Serie
    get() = Serie.Serie01
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.SIM
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}