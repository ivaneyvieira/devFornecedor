package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaFinanceiroViewModel(viewModel: Devolucao01ViewModel) :
  TabDevolucaoViewModelAbstract<IDevolucao01View>(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaFinanceiro
}

interface ITabNotaFinanceiro : ITabNota {
  override val serie: Serie
    get() = Serie.FIN
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}