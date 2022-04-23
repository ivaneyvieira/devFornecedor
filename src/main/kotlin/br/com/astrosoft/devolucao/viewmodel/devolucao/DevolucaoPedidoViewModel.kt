package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPedidoViewModel(view: IDevolucaoPedidoView) : DevolucaoAbstractViewModel<IDevolucaoPedidoView>(view) {
  val tabPedidoBaseViewModel = TabPedidoBaseViewModel(this)
  val tabPedidoPendenteViewModel = TabPedidoPendenteViewModel(this)
  val tabPedidoNFDViewModel = TabPedidoNFDViewModel(this)
  val tabPedidoPagoViewModel = TabPedidoPagoViewModel(this)
  val tabPedidoAjusteViewModel = TabPedidoAjusteViewModel(this)
  val tabPedidoPercaViewModel = TabPedidoPercaViewModel(this)
  val tabPedidoEmailViewModel = TabPedidoEmailViewModel(this)
  val tabPedidoLiberadoViewModel = TabPedidoLiberadoViewModel(this)
  val tabPedidoBaixaViewModel = TabPedidoBaixaViewModel(this)
  val tabPedidoDescarteViewModel = TabPedidoDescarteViewModel(this)

  override fun listTab() = listOf(
    view.tabPedidoBase,
    view.tabPedidoPendente,
    view.tabPedidoLiberado,
    view.tabPedidoEmail,
    view.tabPedidoNFD, //view.tabPedidoPago,
    view.tabPedidoAjuste,
    view.tabPedidoPerca,//view.tabPedidoBaixa,
    view.tabPedidoDescarte,
                                 )
}

interface IDevolucaoPedidoView : IDevolucaoAbstractView {
  val tabPedidoBase: ITabPedidoBase
  val tabPedidoPendente: ITabPedidoPendente
  val tabPedidoNFD: ITabPedidoNFD
  val tabPedidoPago: ITabPedidoPago
  val tabPedidoAjuste: ITabPedidoAjuste
  val tabPedidoPerca: ITabPedidoPerca
  val tabPedidoLiberado: ITabPedidoLiberado
  val tabPedidoEmail: ITabPedidoEmail
  val tabPedidoBaixa: ITabPedidoBaixa
  val tabPedidoDescarte: ITabPedidoDescarte
}

