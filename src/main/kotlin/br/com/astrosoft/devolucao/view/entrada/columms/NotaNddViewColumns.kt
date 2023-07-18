package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaNdd
import br.com.astrosoft.framework.view.*
import com.vaadin.flow.component.grid.Grid

object NotaNddViewColumns {
  fun Grid<NotaEntradaNdd>.notaLoja() = addColumnInt(NotaEntradaNdd::storeno) {
    this.setHeader("Loja")
    this.isResizable = true
  }

  fun Grid<NotaEntradaNdd>.notaNotaSaci() = addColumnString(NotaEntradaNdd::notaFiscal) {
    this.setHeader("Nota Saci")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaEntradaNdd>.notaNumeroPedido() = addColumnInt(NotaEntradaNdd::ordno) {
    this.setHeader("Pedido")
    this.isResizable = true
  }

  fun Grid<NotaEntradaNdd>.notaData() = addColumnLocalDate(NotaEntradaNdd::dataEmissao) {
    this.setHeader("Data")
    this.isResizable = true
  }

  fun Grid<NotaEntradaNdd>.notaTemIPI() = addColumnBool(NotaEntradaNdd::temIPI) {
    this.setHeader("Tem IPI")
    this.isResizable = true
  }

  fun Grid<NotaEntradaNdd>.notaTotal() = addColumnDouble(NotaEntradaNdd::baseCalculoIcms) {
    this.setHeader("Valor")
    this.isResizable = true
  }
}