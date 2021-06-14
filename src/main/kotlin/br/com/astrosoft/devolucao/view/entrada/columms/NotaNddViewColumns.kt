package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaNdd
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaNddViewColumns {
  fun Grid<NotaEntradaNdd>.notaLoja() = addColumnInt(NotaEntradaNdd::storeno) {
    this.setHeader("Loja")
  }

  fun Grid<NotaEntradaNdd>.notaNotaSaci() = addColumnString(NotaEntradaNdd::notaFiscal) {
    this.setHeader("Nota Saci")
  }

  fun Grid<NotaEntradaNdd>.notaNumeroPedido() = addColumnInt(NotaEntradaNdd::ordno) {
    this.setHeader("Pedido")
  }

  fun Grid<NotaEntradaNdd>.notaData() = addColumnLocalDate(NotaEntradaNdd::dataEmissao) {
    this.setHeader("Data")
  }

  fun Grid<NotaEntradaNdd>.notaTotal() = addColumnDouble(NotaEntradaNdd::baseCalculoIcms) {
    this.setHeader("Valor")
  }
}