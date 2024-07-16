package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPedidoViewModel(view: IDevolucaoPedidoView) : DevolucaoAbstractViewModel<IDevolucaoPedidoView>(view) {
  val tabPedidoEditorViewModel = TabPedidoEditorViewModel(this)
  val tabPedidoPendenteViewModel = TabPedidoPendenteViewModel(this)
  val tabMonitoramentoEntradaViewModel = TabMonitoramentoEntradaViewModel(this)
  val tabPedidoFinalizadoViewModel = TabPedidoFinalizadoViewModel(this)
  val tabPedidoUsrViewModel = TabPedidoUsrViewModel(this)

  override fun listTab() = listOf(
    view.tabPedidoPendente,
    view.tabMonitoramentoEntrada,
    view.tabPedidoFinalizado,
    view.tabPedidoEditor,
    view.tabPedidoUsr
  )
}

interface IDevolucaoPedidoView : IDevolucaoAbstractView {
  val tabPedidoEditor: ITabPedidoEditor
  val tabPedidoPendente: ITabPedidoPendente
  val tabMonitoramentoEntrada: ITabMonitoramentoEntrada
  val tabPedidoFinalizado: ITabPedidoFinalizado
  val tabPedidoUsr: ITabPedidoUsr
}

