package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Devolução")
@CssImport("./styles/gridTotal.css")
class DevolucaoView: ViewLayout<DevolucaoViewModel>(), IDevolucaoView {
  override val viewModel: DevolucaoViewModel = DevolucaoViewModel(this)
  override val tabPedido = TabPedido(viewModel.tabPedidoViewModel)
  override val tabEntrada = TabEntrada(viewModel.tabEntradaViewModel)
  override val tabNotaSerie66 = TabNotaSerie66(viewModel.tabNotaDevolucaoViewModel)
  override val tabNotaSerie66Pago = TabNotaSerie66Pago(viewModel.tabNota66Pago)
  override val tabNotaSerie01 = TabNotaSerie01(viewModel.tabNotaVendaViewModel)
  override val tabEmailRecebido = TabEmailRecebido(viewModel.tabEmailRecebido)
  
  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.permissoes.menuDevolucao
  }
  
  init {
    addTabSheat(viewModel)
  }
}

