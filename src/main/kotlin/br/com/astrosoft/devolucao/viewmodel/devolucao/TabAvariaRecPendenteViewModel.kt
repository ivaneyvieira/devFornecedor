package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabAvariaRecPendenteViewModel(viewModel: DevolucaoAvariaRecViewModel) :
  TabDevolucaoViewModelAbstract<IDevolucaoAvariaRecView>(viewModel) {
  override val subView
    get() = viewModel.view.tabAvariaRecPendente
}

interface ITabAvariaRecPendente : ITabNota {
  override val serie: Serie
    get() = Serie.AVA
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NONE
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}