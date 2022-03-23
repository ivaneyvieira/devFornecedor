package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.Devolucao01ViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoInternaViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoInternaView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Interna")
@CssImport("./styles/gridTotal.css")
class DevolucaoInternaView : ViewLayout<DevolucaoInternaViewModel>(), IDevolucaoInternaView {
  override val viewModel: DevolucaoInternaViewModel = DevolucaoInternaViewModel(this)

  override val tabAjusteGarantia = TabAjusteGarantia(viewModel.tabAjusteGarantiaViewModel)
  override val tabAjusteGarantiaPago = TabAjusteGarantiaPago(viewModel.tabAjusteGarantiaPagoViewModel)
  override val tabAjusteGarantiaPerca = TabAjusteGarantiaPerca(viewModel.tabAjusteGarantiaPercaViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucaoInterna
  }

  init {
    addTabSheat(viewModel)
  }
}

