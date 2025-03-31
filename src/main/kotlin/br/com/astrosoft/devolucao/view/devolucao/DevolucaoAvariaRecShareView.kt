package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoAvariaRecViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAvariaRecView
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.session.SecurityUtils
import br.com.astrosoft.framework.session.Session
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import kotlin.jvm.optionals.getOrNull

@Route(value = "avariarec/:login/:senha")
@PageTitle("Avaria Recebimento")
@CssImport("./styles/gridTotal.css")
class DevolucaoAvariaRecShareView : ViewLayout<DevolucaoAvariaRecViewModel>(), IDevolucaoAvariaRecView,
  BeforeEnterObserver {
  override val viewModel: DevolucaoAvariaRecViewModel = DevolucaoAvariaRecViewModel(this)
  override val tabAvariaRecEditor = TabAvariaRecEditor(viewModel.tabAvariaRecEditorViewModel)
  override val tabAvariaRecPendente = TabAvariaRecPendente(viewModel.tabAvariaRecPendenteViewModel)
  override val tabAvariaRecTransportadora = TabAvariaRecTransportadora(viewModel.tabAvariaRecTransportadoraViewModel)
  override val tabAvariaRecEmail = TabAvariaRecEmail(viewModel.tabAvariaRecEmailViewModel)
  override val tabAvariaRecAcerto = TabAvariaRecAcerto(viewModel.tabAvariaRecAcertoViewModel)
  override val tabAvariaRecReposto = TabAvariaRecReposto(viewModel.tabAvariaRecRepostoViewModel)
  override val tabAvariaRecNFD = TabAvariaRecNFD(viewModel.tabAvariaRecNFDViewModel)
  override val tabAvariaRecUsr = TabAvariaRecUsr(viewModel.tabAvariaRecUsrViewModel)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.menuDevolucaoAvariaRec
  }

  init {
    setSizeFull()
  }

  override fun beforeEnter(event: BeforeEnterEvent?) {
    val login = event?.routeParameters?.get("login")?.getOrNull()
    val senha = event?.routeParameters?.get("senha")?.getOrNull()

    if (SecurityUtils.isUserLoggedIn) {
      val user = Config.user
      if (user?.login != login) {
        Session.current.close()
        ui.ifPresent {
          it.session.close()
        }
      }
      Session.current.close()
      ui.ifPresent {
        it.session.close()
      }
    }
    SecurityUtils.login(login, senha)
    addTabSheat(viewModel)
  }
}

