package br.com.astrosoft.devolucao.view.saida.columns

import br.com.astrosoft.devolucao.model.beans.NotaSaidaNdd
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object SaidaNddColumns {
  fun Grid<NotaSaidaNdd>.lojaNotaSaida() = addColumnInt(NotaSaidaNdd::loja) {
    this.setHeader("Loja")
  }

  fun Grid<NotaSaidaNdd>.notaNotaSaida() = addColumnString(NotaSaidaNdd::nota) {
    this.setHeader("Nota")
  }

  fun Grid<NotaSaidaNdd>.dataNotaSaida() = addColumnLocalDate(NotaSaidaNdd::data) {
    this.setHeader("Data")
  }

  fun Grid<NotaSaidaNdd>.pedidoNotaSaida() = addColumnInt(NotaSaidaNdd::pedido) {
    this.setHeader("Pedido")
  }

  fun Grid<NotaSaidaNdd>.codigoClienteNotaSaida() = addColumnInt(NotaSaidaNdd::codigoCliente) {
    this.setHeader("Código")
  }

  fun Grid<NotaSaidaNdd>.nomeClienteNotaSaida() = addColumnString(NotaSaidaNdd::nomeCliente) {
    this.setHeader("Nome do Cliente")
  }

  fun Grid<NotaSaidaNdd>.valorTotalNotaSaida() = addColumnDouble(NotaSaidaNdd::valor) {
    this.setHeader("Valor")
  }

  fun Grid<NotaSaidaNdd>.chaveNotaSaida() = addColumnString(NotaSaidaNdd::chave) {
    this.setHeader("Chave")
  }
}