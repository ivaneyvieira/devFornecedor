package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie66ViewModel(viewModel: DevolucaoViewModel) : TabDevolucaoViewModelAbstract(
  viewModel
                                                                                            ) {
  override val subView
    get() = viewModel.view.tabNotaSerie66
}

interface ITabNotaSerie66 : ITabNota {
  override val serie: Serie
    get() = Serie.Serie66
  override val pago66: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}
