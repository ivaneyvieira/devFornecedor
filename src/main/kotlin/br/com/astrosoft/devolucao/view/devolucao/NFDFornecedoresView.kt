package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.INFDFornecedoresView
import br.com.astrosoft.devolucao.viewmodel.devolucao.NFDFornecedoresViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("NFD Fornecedores")
@CssImport("./styles/gridTotal.css")
class NFDFornecedoresView : ViewLayout<NFDFornecedoresViewModel>(), INFDFornecedoresView {
  override val viewModel: NFDFornecedoresViewModel = NFDFornecedoresViewModel(this)

  override val tabNFDFornecedoresEditor = TabNFDFornecedoresEditor(viewModel.tabNFDFornecedoresEditorViewModel)
  override val tabNFDFornecedoresAberta = TabNFDFornecedoresAberta(viewModel.tabNFDFornecedoresAbertaViewModel)
  override val tabNFDFornecedoresPaga = TabNFDFornecedoresPaga(viewModel.tabNFDFornecedoresPagaViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucao01
  }

  init {
    addTabSheat(viewModel)
  }
}

