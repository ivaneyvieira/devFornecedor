package br.com.astrosoft.devolucao.view.compra.columns

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object PedidoCompraColumns {
  fun Grid<PedidoCompra>.colLoja() = addColumnInt(PedidoCompra::loja) {
    this.setHeader("Loja")
  }

  fun Grid<PedidoCompra>.colNumeroPedido() = addColumnInt(PedidoCompra::numeroPedido) {
    this.setHeader("Pedido")
  }

  fun Grid<PedidoCompra>.colVlPedida() = addColumnDouble(PedidoCompra::vlPedido) {
    this.setHeader("Vl Pedido")
  }

  fun Grid<PedidoCompra>.colVlCancelada() = addColumnDouble(PedidoCompra::vlCancelado) {
    this.setHeader("Vl Cancelado")
  }

  fun Grid<PedidoCompra>.colVlRecebida() = addColumnDouble(PedidoCompra::vlRecebido) {
    this.setHeader("Vl Recebido")
  }

  fun Grid<PedidoCompra>.colVlPendente() = addColumnDouble(PedidoCompra::vlPendente) {
    this.setHeader("Vl Pendente")
  }

  fun Grid<PedidoCompra>.colDataPedido() = addColumnLocalDate(PedidoCompra::dataPedido) {
    this.setHeader("Data Pedido")
  }

  fun Grid<PedidoCompra>.colDataEntrega() = addColumnLocalDate(PedidoCompra::dataEntrega) {
    this.setHeader("Data Entrega")
  }

  fun Grid<PedidoCompra>.colObservacao() = addColumnString(PedidoCompra::obsercacaoPedido) {
    this.setHeader("Observação")
  }
}