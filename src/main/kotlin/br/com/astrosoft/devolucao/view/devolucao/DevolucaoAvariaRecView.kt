package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoAvariaRecViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAvariaRecView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Avaria Recebimento")
@CssImport("./styles/gridTotal.css")
class DevolucaoAvariaRecView : ViewLayout<DevolucaoAvariaRecViewModel>(), IDevolucaoAvariaRecView {
  override val viewModel: DevolucaoAvariaRecViewModel = DevolucaoAvariaRecViewModel(this)
  override val tabAvariaRecEditor = TabAvariaRecEditor(viewModel.tabAvariaRecEditorViewModel)
  override val tabAvariaRecPendente = TabAvariaRecPendente(viewModel.tabAvariaRecPendenteViewModel)
  override val tabAvariaRecTransportadora = TabAvariaRecTransportadora(viewModel.tabAvariaRecTransportadoraViewModel)
  override val tabAvariaRecEmail = TabAvariaRecEmail(viewModel.tabAvariaRecEmailViewModel)
  override val tabAvariaRecUsr = TabAvariaRecUsr(viewModel.tabAvariaRecUsrViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucaoAvariaRec
  }

  init {
    addTabSheat(viewModel)
  }
}

