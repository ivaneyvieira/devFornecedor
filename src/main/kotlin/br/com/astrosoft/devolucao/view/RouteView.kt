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
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route

@Route("", layout = DevFornecedorLayout::class)
class RouteView : Div(), BeforeEnterObserver {
  override fun beforeEnter(event: BeforeEnterEvent?) {
    val user = Config.user as? UserSaci
    if (user != null) {
      when {
        user.menuDevolucao01      -> event?.rerouteTo(Devolucao01View::class.java)
        user.menuDevolucaoInterna -> event?.rerouteTo(DevolucaoInternaView::class.java)
        user.menuDevolucaoPedido  -> event?.rerouteTo(DevolucaoPedidoView::class.java)
        user.menuDevolucao66      -> event?.rerouteTo(Devolucao66View::class.java)
        user.menuCompra           -> event?.rerouteTo(CompraView::class.java)
        user.menuPreEntrada       -> event?.rerouteTo(PreEntradaView::class.java)
        user.menuEntrada          -> event?.rerouteTo(EntradaView::class.java)
        user.menuSaida            -> event?.rerouteTo(SaidaView::class.java)
        user.menuRecebimento      -> event?.rerouteTo(RecebimentoView::class.java)
        user.menuDemanda          -> event?.rerouteTo(DemandaView::class.java)
        user.menuAgenda           -> event?.rerouteTo(AgendaView::class.java)
        user.admin                -> event?.rerouteTo(UsuarioView::class.java)
        user.admin                -> event?.rerouteTo(AssinaturaView::class.java)
      }
    }
  }
}