package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNFDFornecedoresPagaViewModel(viewModel: NFDFornecedoresViewModel) :
  TabDevolucaoViewModelAbstract<INFDFornecedoresView>(viewModel) {
  override val subView
    get() = viewModel.view.tabNFDFornecedoresPaga
}

interface ITabNFDFornecedoresPagaViewModel : ITabNota {
  override val serie: Serie
    get() = Serie.NFD
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.SIM
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}