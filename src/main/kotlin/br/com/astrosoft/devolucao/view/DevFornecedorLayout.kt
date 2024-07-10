package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.agenda.AgendaView
import br.com.astrosoft.devolucao.view.compra.CompraView
import br.com.astrosoft.devolucao.view.demanda.DemandaView
import br.com.astrosoft.devolucao.view.devolucao.Devolucao01View
import br.com.astrosoft.devolucao.view.devolucao.Devolucao66View
import br.com.astrosoft.devolucao.view.devolucao.DevolucaoInternaView
import br.com.astrosoft.devolucao.view.devolucao.DevolucaoPedidoView
import br.com.astrosoft.devolucao.view.entrada.EntradaView
import br.com.astrosoft.devolucao.view.preentrada.PreEntradaView
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
    val user = Config.user as? UserSaci
    if (user?.menuDevolucao01 == true)
      menuRoute(FORM, "NFD", Devolucao01View::class)
    if (user?.menuDevolucaoInterna == true)
      menuRoute(FORM, "Interna", DevolucaoInternaView::class)
    if (user?.menuDevolucaoPedido == true)
      menuRoute(FORM, "Pedido", DevolucaoPedidoView::class)
    if (user?.menuDevolucao66 == true)
      menuRoute(FORM, "Série 66", Devolucao66View::class)
    if (user?.menuCompra == true)
      menuRoute(FORM, "Compra", CompraView::class)
    if (user?.menuPreEntrada == true)
      menuRoute(FORM, "Pré-entrada", PreEntradaView::class)
    if (user?.menuEntrada == true)
      menuRoute(FORM, "Entrada", EntradaView::class)
    if (user?.menuSaida == true)
      menuRoute(FORM, "Saida", SaidaView::class)
    if (user?.menuRecebimento == true)
      menuRoute(TRUCK, "Recebimento", RecebimentoView::class)
    if (user?.menuDemanda == true)
      menuRoute(CLOCK, "Demanda", DemandaView::class)
    if (user?.menuAgenda == true)
      menuRoute(CLOCK, "Agenda", AgendaView::class)
    if (user?.admin == true)
      menuRoute(USER, "Usuário", UsuarioView::class, Config.isAdmin)
    if (user?.admin == true)
      menuRoute(SIGN_IN, "Assinatura", AssinaturaView::class, Config.isAdmin)
  }
}