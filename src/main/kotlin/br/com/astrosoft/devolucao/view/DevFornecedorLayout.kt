package br.com.astrosoft.devolucao.view

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.view.agenda.AgendaView
import br.com.astrosoft.devolucao.view.devolucao.DevolucaoView
import br.com.astrosoft.devolucao.view.recebimento.RecebimentoView
import br.com.astrosoft.devolucao.view.teste.AssinaturaView
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
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import org.springframework.security.core.context.SecurityContextHolder

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@PWA(name = AppConfig.title,
     shortName = AppConfig.shortName,
     iconPath = AppConfig.iconPath,
     enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js") class DevFornecedorLayout : AppLayout() {
  init {
    isDrawerOpened = true
    navbar {
      drawerToggle()
      h3(AppConfig.title)
      horizontalLayout {
        isExpand = true
      } //anchor("logout", "Sair")
      button("Sair") {
        onLeftClick {
          SecurityContextHolder.clearContext()
          ui.ifPresent {
            it.session.close()
            it.navigate("")
          }
        }
      }
    }
    drawer {
      verticalLayout {
        label("Versão ${AppConfig.version}")
        label(AppConfig.user?.login)
      }
      hr()

      tabs {
        orientation = Tabs.Orientation.VERTICAL
        tab {
          this.icon(FORM)
          routerLink(text = "Devolução", viewType = DevolucaoView::class)
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
          this.isEnabled = AppConfig.isAdmin
          this.icon(USER)
          routerLink(text = "Usuário", viewType = UsuarioView::class)
        }
        tab {
          this.isEnabled = AppConfig.isAdmin
          this.icon(SIGN_IN)
          routerLink(text = "Assinatura", viewType = AssinaturaView::class)
        }
      }
    }
  }
}