package br.com.astrosoft.devolucao.view.preentrada

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.preentrada.IPreEntradaView
import br.com.astrosoft.devolucao.viewmodel.preentrada.ITabPreRefFiscalViewModel
import br.com.astrosoft.devolucao.viewmodel.preentrada.PreEntradaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Pré-entrada")
@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class PreEntradaView() : ViewLayout<PreEntradaViewModel>(), IPreEntradaView {
  override val viewModel: PreEntradaViewModel = PreEntradaViewModel(this)
  override val tabTribFiscalPreViewModel = TabTribFiscalPre(viewModel.tabTribFiscalPreViewModel)
  override val tabPreRefFiscalViewModel = TabPreRefFiscal(viewModel.tabPreRefFiscalViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuPreEntrada
  }

  init {
    addTabSheat(viewModel)
  }
}

