package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPedidoViewModel(view: IDevolucaoPedidoView) : DevolucaoAbstractViewModel<IDevolucaoPedidoView>(view) {
  val tabPedidoViewModel = TabPedidoViewModel(this)
  val tabPedidoNFDViewModel = TabPedidoNFDViewModel(this)
  val tabPedidoPagoViewModel = TabPedidoPagoViewModel(this)
  val tabPedidoAjusteViewModel = TabPedidoAjusteViewModel(this)
  val tabPedidoBaixaViewModel = TabPedidoBaixaViewModel(this)

  override fun listTab() = listOf(
    view.tabPedido,
    view.tabPedidoNFD,
    view.tabPedidoPago,
    view.tabPedidoAjuste,
    view.tabPedidoBaixa,
                                 )
}

interface IDevolucaoPedidoView : IDevolucaoAbstractView {
  val tabPedido: ITabPedido
  val tabPedidoNFD: ITabPedidoNFD
  val tabPedidoPago: ITabPedidoPago
  val tabPedidoAjuste: ITabPedidoAjuste
  val tabPedidoBaixa: ITabPedidoBaixa
}

