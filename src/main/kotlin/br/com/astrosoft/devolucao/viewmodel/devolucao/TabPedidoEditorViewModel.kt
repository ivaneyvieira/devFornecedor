package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabPedidoEditorViewModel(viewModel: DevolucaoPedidoViewModel) :
        TabDevolucaoViewModelAbstract<IDevolucaoPedidoView>(viewModel) {
  override val subView
    get() = viewModel.view.tabPedidoEditor
}

interface ITabPedidoEditor : ITabNota {
  override val serie: Serie
    get() = Serie.PED
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}