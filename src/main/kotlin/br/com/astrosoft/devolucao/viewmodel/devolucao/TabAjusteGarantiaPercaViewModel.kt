package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabAjusteGarantiaPercaViewModel(viewModel: DevolucaoInternaViewModel) :
        TabDevolucaoViewModelAbstract<IDevolucaoInternaView>(viewModel) {
  override val subView
    get() = viewModel.view.tabAjusteGarantiaPago
}

interface ITabAjusteGarantiaPerca : ITabNota {
  override val serie: Serie
    get() = Serie.AJP
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}