package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaPendenteSerie01ViewModel(viewModel: DevolucaoPendenteViewModel) :
        TabDevolucaoViewModelAbstract<IDevolucaoPendenteView>(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaPendenteSerie01
}

interface ITabNotaPendenteSerie01 : ITabNota {
  override val serie: Serie
    get() = Serie.Serie01
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}