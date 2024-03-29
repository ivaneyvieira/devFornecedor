package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNFDFornecedoresAbertaViewModel(viewModel: NFDFornecedoresViewModel) :
  TabDevolucaoViewModelAbstract<INFDFornecedoresView>(viewModel) {
  override val subView
    get() = viewModel.view.tabNFDFornecedoresAberta
}

interface ITabNFDFornecedoresAbertaViewModel : ITabNota {
  override val serie: Serie
    get() = Serie.NFD
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}