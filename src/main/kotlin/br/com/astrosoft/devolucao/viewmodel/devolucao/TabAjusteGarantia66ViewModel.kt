package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabAjusteGarantia66ViewModel(viewModel: DevolucaoInternaViewModel) :
  TabDevolucaoViewModelAbstract<IDevolucaoInternaView>(viewModel) {
  override val subView
    get() = viewModel.view.tabAjusteGarantia66
}

interface ITabAjusteGarantia66 : ITabNota {
  override val serie: Serie
    get() = Serie.A66
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}