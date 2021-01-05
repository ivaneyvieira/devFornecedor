package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevFornecedorViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevFornecedorView
import br.com.astrosoft.framework.view.ITabPanel
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Devolução")
@CssImport("./styles/gridTotal.css")
class DevFornecedorView: ViewLayout<DevFornecedorViewModel>(), IDevFornecedorView {
  override val viewModel: DevFornecedorViewModel = DevFornecedorViewModel(this)
  override val tabPedido = TabPedido(viewModel.tabPedidoViewModel)
  override val tabNotaSerie66 = TabNotaSerie66(viewModel.tabNotaDevolucaoViewModel)
  override val tabNotaSerie66Pago = TabNotaSerie66Pago(viewModel.tabNota66Pago)
  override val tabNotaSerie01 = TabNotaSerie01(viewModel.tabNotaVendaViewModel)
  override val tabEmailRecebido = TabEmailRecebido(viewModel.tabEmailRecebido)
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    tabSheet {
      setSizeFull()
      val tabs = viewModel.tabsAuthorized()
      tabs.forEach {tab ->
        tabPanel(tab as ITabPanel)
      }
      tabs.firstOrNull()?.updateComponent()
    }
  }
}

