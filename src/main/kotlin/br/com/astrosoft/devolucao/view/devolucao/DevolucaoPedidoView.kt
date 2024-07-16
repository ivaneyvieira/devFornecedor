package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoPedidoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPedidoView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Pedido")
@CssImport("./styles/gridTotal.css")
class DevolucaoPedidoView : ViewLayout<DevolucaoPedidoViewModel>(), IDevolucaoPedidoView {
  override val viewModel: DevolucaoPedidoViewModel = DevolucaoPedidoViewModel(this)
  override val tabPedidoEditor = TabPedidoEditor(viewModel.tabPedidoEditorViewModel)
  override val tabPedidoPendente = TabPedidoPendente(viewModel.tabPedidoPendenteViewModel)
  override val tabPedidoFinalizado = TabPedidoFinalizado(viewModel.tabPedidoFinalizadoViewModel)
  override val tabPedidoUsr = TabPedidoUsr(viewModel.tabPedidoUsrViewModel)
  override val tabMonitoramentoEntrada = TabMonitoramentoEntrada(viewModel.tabMonitoramentoEntradaViewModel)
  //override val tabPedidoFornecedor = TabPedidoFornecedor(viewModel.tabPedidoFornecedorViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucaoPedido
  }

  init {
    addTabSheat(viewModel)
  }
}

