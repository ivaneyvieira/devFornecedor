package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.entrada.EntradaViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.IEntradaView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Entrada")
@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class EntradaView : ViewLayout<EntradaViewModel>(), IEntradaView {
  override val viewModel: EntradaViewModel = EntradaViewModel(this)
  override val tabEntradaNddViewModel = TabEntradaNdd(viewModel.tabEntradaNddViewModel)
  override val tabEntradaNddReceberViewModel = TabEntradaNddReceber(viewModel.tabEntradaNddReceberViewModel)
  override val tabEntradaNddRecebidoViewModel = TabEntradaNddRecebido(viewModel.tabEntradaNddRecebidoViewModel)
  override val tabTribFiscalViewModel = TabTribFiscal(viewModel.tabTribFiscalViewModel)
  override val tabSpedViewModel = TabSped(viewModel.tabSpedViewModel)
  override val tabSubstiFcViewModel = TabSubstiFc(viewModel.tabSubstiFcViewModel)
  override val tabSped2ViewModel = TabSped2(viewModel.tabSped2ViewModel)
  override val tabXmlTribViewModel = TabXmlTrib(viewModel.tabXmlTribViewModel)
  override val tabSTEstadoViewModel = TabSTEstado(viewModel.tabSTEstadoViewModel)
  override val tabFreteViewModel = TabFrete(viewModel.tabFreteViewModel)
  override val tabFretePerViewModel = TabFretePer(viewModel.tabFretePerViewModel)
  override val tabCteViewModel = TabCte(viewModel.tabCteViewModel)
  override val tabPrecoViewModel = TabPreco(viewModel.tabPrecoViewModel)
  override val tabFileNFEViewModel = TabFileNFE(viewModel.tabFileNFEViewModel)
  override val tabRefFiscalViewModel = TabRefFiscal(viewModel.tabRefFiscalViewModel)
  override val tabTodasEntradasViewModel = TabTodasEntradas(viewModel.tabTodasEntradasViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuEntrada
  }

  init {
    addTabSheat(viewModel, 3)
  }
}

