package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabAjusteGarantiaViewModel(viewModel: DevolucaoInternaViewModel) :
  TabDevolucaoViewModelAbstract<IDevolucaoInternaView>(viewModel) {
  override val subView
    get() = viewModel.view.tabAjusteGarantia
}

interface ITabAjusteGarantia : ITabNota {
  override val serie: Serie
    get() = Serie.AJT
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}