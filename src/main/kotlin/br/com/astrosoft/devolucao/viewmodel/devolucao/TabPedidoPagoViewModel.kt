package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabPedidoPagoViewModel(viewModel: DevolucaoPedidoViewModel) :
        TabDevolucaoViewModelAbstract<IDevolucaoPedidoView>(viewModel) {
  override val subView
    get() = viewModel.view.tabPedidoPago
}

interface ITabPedidoPago : ITabNota {
  override val serie: Serie
    get() = Serie.PED
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.SIM
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}