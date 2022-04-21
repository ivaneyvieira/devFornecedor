package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPedidoViewModel(view: IDevolucaoPedidoView) : DevolucaoAbstractViewModel<IDevolucaoPedidoView>(view) {
  val tabPedidoBaseViewModel = TabPedidoBaseViewModel(this)
  val tabPedidoPendenteViewModel = TabPedidoPendenteViewModel(this)
  val tabPedidoNFDViewModel = TabPedidoNFDViewModel(this)
  val tabPedidoPagoViewModel = TabPedidoPagoViewModel(this)
  val tabPedidoAjusteViewModel = TabPedidoAjusteViewModel(this)
  val tabPedidoEmailViewModel = TabPedidoEmailViewModel(this)
  val tabPedidoBaixaViewModel = TabPedidoBaixaViewModel(this)

  override fun listTab() = listOf(
    view.tabPedidoBase,
    view.tabPedidoPendente,
    view.tabPedidoEmail,
    view.tabPedidoNFD, //view.tabPedidoPago,
    view.tabPedidoAjuste, //view.tabPedidoBaixa,
                                 )
}

interface IDevolucaoPedidoView : IDevolucaoAbstractView {
  val tabPedidoBase: ITabPedidoBase
  val tabPedidoPendente: ITabPedidoPendente
  val tabPedidoNFD: ITabPedidoNFD
  val tabPedidoPago: ITabPedidoPago
  val tabPedidoAjuste: ITabPedidoAjuste
  val tabPedidoEmail: ITabPedidoEmail
  val tabPedidoBaixa: ITabPedidoBaixa
}

