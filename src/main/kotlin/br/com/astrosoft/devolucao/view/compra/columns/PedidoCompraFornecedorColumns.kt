package br.com.astrosoft.devolucao.view.compra.columns

import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object PedidoCompraFornecedorColumns {
  fun Grid<PedidoCompraFornecedor>.colCodigo() = addColumnInt(PedidoCompraFornecedor::vendno) {
    this.setHeader("Cód Forn")
  }

  fun Grid<PedidoCompraFornecedor>.colFornecedor() = addColumnString(PedidoCompraFornecedor::fornecedor) {
    this.setHeader("Fornecedor")
  }

  fun Grid<PedidoCompraFornecedor>.colVlPedida() = addColumnDouble(PedidoCompraFornecedor::vlPedido) {
    this.setHeader("Vl Pedido")
  }

  fun Grid<PedidoCompraFornecedor>.colVlCancelada() = addColumnDouble(PedidoCompraFornecedor::vlCancelado) {
    this.setHeader("Vl Cancelado")
  }

  fun Grid<PedidoCompraFornecedor>.colVlRecebida() = addColumnDouble(PedidoCompraFornecedor::vlRecebido) {
    this.setHeader("Vl Recebido")
  }

  fun Grid<PedidoCompraFornecedor>.colVlPendente() = addColumnDouble(PedidoCompraFornecedor::vlPendente) {
    this.setHeader("Vl Pendente")
  }

  fun Grid<PedidoCompraFornecedor>.colDataPedido() = addColumnLocalDate(PedidoCompraFornecedor::dataPedido) {
    this.setHeader("Data Pedido")
  }
}