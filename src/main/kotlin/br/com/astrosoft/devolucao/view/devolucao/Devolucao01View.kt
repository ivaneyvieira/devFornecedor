package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.Devolucao01ViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Série 01")
@CssImport("./styles/gridTotal.css")
class Devolucao01View : ViewLayout<Devolucao01ViewModel>(), IDevolucao01View {
  override val viewModel: Devolucao01ViewModel = Devolucao01ViewModel(this)

  override val tabNotaRemessaConserto = TabNotaRemessaConserto(viewModel.tabNotaRemessaConsertoViewModel)
  override val tabNotaSerie01 = TabNotaSerie01(viewModel.tabNotaSerie01ViewModel)
  override val tabNotaSerie01Pago = TabNotaSerie01Pago(viewModel.tabNotaSerie01PagoViewModel)
  //override val tabNotaSerie01Coleta = TabNotaSerie01Coleta(viewModel.tabNotaSerie01ColetaViewModel)
  override val tabNotaFinanceiro = TabNotaFinanceiro(viewModel.tabNotaFinanceiroViewModel)
  override val tabConferenciaSap = TabConferenciaSap(viewModel.tabConferenciaSap)
  override val tabSap = TabSap(viewModel.tabSap)
  override val tabDesconto = TabDesconto(viewModel.tabDesconto)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucao01
  }

  init {
    addTabSheat(viewModel)
  }
}

