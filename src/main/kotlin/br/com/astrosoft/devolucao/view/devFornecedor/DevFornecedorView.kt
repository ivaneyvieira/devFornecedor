package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevFornecedorViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevFornecedorView
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Devolução")
class DevFornecedorView: ViewLayout<DevFornecedorViewModel>(), IDevFornecedorView {
  override val viewModel: DevFornecedorViewModel = DevFornecedorViewModel(this)
  override val tabNotaDevolucao = TabNotaDevolucao(viewModel.tabNotaDevolucaoViewModel)
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    val ts = tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      if(user?.entrega_imprimir == true)
        tabPanel(tabNotaDevolucao)
    }
  }
}

