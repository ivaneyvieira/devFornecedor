package br.com.astrosoft.devolucao.view.saida

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.saida.ISaidaView
import br.com.astrosoft.devolucao.viewmodel.saida.SaidaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Saída")
@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class SaidaView : ViewLayout<SaidaViewModel>(), ISaidaView {
  override val viewModel: SaidaViewModel = SaidaViewModel(this)
  override val tabSaidaNddViewModel = TabSaidaNdd(viewModel.tabSaidaNddViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuSaida
  }

  init {
    addTabSheat(viewModel)
  }
}

