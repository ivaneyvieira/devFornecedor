package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPedidoViewModel(view: IDevolucaoPedidoView) : DevolucaoAbstractViewModel<IDevolucaoPedidoView>(view) {
  val tabPedidoEditorViewModel = TabPedidoEditorViewModel(this)
  val tabPedidoPendenteViewModel = TabPedidoPendenteViewModel(this)
  val tabPedidoFinalizadoViewModel = TabPedidoFinalizadoViewModel(this)

  override fun listTab() = listOf(
    view.tabPedidoEditor,
    view.tabPedidoPendente,
    view.tabPedidoFinalizado,
  )
}

interface IDevolucaoPedidoView : IDevolucaoAbstractView {
  val tabPedidoEditor: ITabPedidoEditor
  val tabPedidoPendente: ITabPedidoPendente
  val tabPedidoFinalizado: ITabPedidoFinalizado
}

