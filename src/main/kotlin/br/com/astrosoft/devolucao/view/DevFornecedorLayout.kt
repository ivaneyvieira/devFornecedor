package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.view.agenda.AgendaView
import br.com.astrosoft.devolucao.view.devolucao.Devolucao01View
import br.com.astrosoft.devolucao.view.devolucao.Devolucao66View
import br.com.astrosoft.devolucao.view.recebimento.RecebimentoView
import br.com.astrosoft.devolucao.view.teste.AssinaturaView
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.session.LoginView
import br.com.astrosoft.framework.session.SecurityUtils
import br.com.astrosoft.framework.session.Session
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.drawer
import com.github.mvysny.karibudsl.v10.drawerToggle
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.hr
import com.github.mvysny.karibudsl.v10.icon
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.label
import com.github.mvysny.karibudsl.v10.navbar
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.routerLink
import com.github.mvysny.karibudsl.v10.tab
import com.github.mvysny.karibudsl.v10.tabs
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@JsModule("./styles/shared-styles.js")
class DevFornecedorLayout : AppLayout(), RouterLayout, BeforeEnterObserver {
  init {
    isDrawerOpened = true
    navbar {
      drawerToggle()
      h3(Config.title)
      horizontalLayout {
        isExpand = true
      }
      button("Sair") {
        onLeftClick {
          Session.current.close()
          ui.ifPresent {
            it.session.close()
            it.navigate("")
          }
        }
      }
    }
    drawer {
      verticalLayout {
        label("Versão ${Config.version}")
        label(Config.user?.login)
      }
      hr()

      tabs {
        orientation = Tabs.Orientation.VERTICAL
        tab {
          this.icon(FORM)
          routerLink(text = "Devolução Série 1", viewType = Devolucao01View::class)
        }
        tab {
          this.icon(FORM)
          routerLink(text = "Devolução Série 66", viewType = Devolucao66View::class)
        }

        tab {
          this.icon(TRUCK)
          routerLink(text = "Recebimento", viewType = RecebimentoView::class)
        }

        tab {
          this.icon(CLOCK)
          routerLink(text = "Agenda", viewType = AgendaView::class)
        }

        tab {
          this.isEnabled = Config.isAdmin
          this.icon(USER)
          routerLink(text = "Usuário", viewType = UsuarioView::class)
        }
        tab {
          this.isEnabled = Config.isAdmin
          this.icon(SIGN_IN)
          routerLink(text = "Assinatura", viewType = AssinaturaView::class)
        }
      }
    }
  }

  override fun beforeEnter(event: BeforeEnterEvent) {
    if (!SecurityUtils.isUserLoggedIn) {
      event.rerouteTo(LoginView::class.java)
    }
  }
}