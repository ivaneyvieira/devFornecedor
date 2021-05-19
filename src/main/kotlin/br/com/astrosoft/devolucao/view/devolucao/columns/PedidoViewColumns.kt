package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.Pedido
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import com.vaadin.flow.component.grid.Grid

object PedidoViewColumns {
  fun Grid<Pedido>.pedidoLoja() = addColumnInt(Pedido::loja) {
    this.setHeader("Loja")
  }

  fun Grid<Pedido>.pedidoNumero() = addColumnInt(Pedido::pedido) {
    this.setHeader("Pedido")
  }

  fun Grid<Pedido>.pedidoData() = addColumnLocalDate(Pedido::data) {
    this.setHeader("Data")
  }

  fun Grid<Pedido>.pedidoTotal() = addColumnDouble(Pedido::total) {
    this.setHeader("Total")
  }
}