package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.Devolucao66ViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao66View
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class) @PageTitle("Devolução") @CssImport("./styles/gridTotal.css")
class Devolucao66View : ViewLayout<Devolucao66ViewModel>(), IDevolucao66View {
  override val viewModel: Devolucao66ViewModel = Devolucao66ViewModel(this)

  override val tabEntrada = TabEntrada(viewModel.tabEntradaViewModel)
  override val tabNotaSerie66 = TabNotaSerie66(viewModel.tabNotaDevolucaoViewModel)
  override val tabNotaSerie66Pago = TabNotaSerie66Pago(viewModel.tabNota66PagoViewModel)
  override val tabEmailRecebido = TabEmailRecebido(viewModel.tabEmailRecebidoViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucao66
  }

  init {
    addTabSheat(viewModel)
  }
}

