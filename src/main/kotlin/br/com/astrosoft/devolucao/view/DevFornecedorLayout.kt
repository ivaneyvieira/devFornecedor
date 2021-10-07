package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.view.agenda.AgendaView
import br.com.astrosoft.devolucao.view.devolucao.Devolucao01View
import br.com.astrosoft.devolucao.view.devolucao.Devolucao66View
import br.com.astrosoft.devolucao.view.devolucao.DevolucaoPendenteView
import br.com.astrosoft.devolucao.view.entrada.EntradaView
import br.com.astrosoft.devolucao.view.recebimento.RecebimentoView
import br.com.astrosoft.devolucao.view.saida.SaidaView
import br.com.astrosoft.devolucao.view.teste.AssinaturaView
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.view.MainLayout
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@JsModule("./styles/shared-styles.js")
class DevFornecedorLayout : MainLayout() {
  override fun Tabs.menuConfig() {
    menuRoute(FORM, "Dev For Pendentes", DevolucaoPendenteView::class)
    menuRoute(FORM, "Devolução Série 1", Devolucao01View::class)
    menuRoute(FORM, "Devolução Série 66", Devolucao66View::class)
    menuRoute(FORM, "Entrada", EntradaView::class)
    menuRoute(FORM, "Saida", SaidaView::class)
    menuRoute(TRUCK, "Recebimento", RecebimentoView::class)
    menuRoute(CLOCK, "Agenda", AgendaView::class)
    menuRoute(USER, "Usuário", UsuarioView::class, Config.isAdmin)
    menuRoute(SIGN_IN, "Assinatura", AssinaturaView::class, Config.isAdmin)
  }
}