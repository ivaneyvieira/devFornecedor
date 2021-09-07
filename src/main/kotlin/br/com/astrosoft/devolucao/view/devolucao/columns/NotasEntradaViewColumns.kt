package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaEntradaNdd
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotasEntradaViewColumns {
  fun Grid<NotaEntradaNdd>.pedidoNumero() = addColumnString(NotaEntradaNdd::notaFiscal) {
    this.setHeader("Nota")
  }

  fun Grid<NotaEntradaNdd>.pedidoData() = addColumnLocalDate(NotaEntradaNdd::dataEmissao) {
    this.setHeader("Emissão")
  }

  fun Grid<NotaEntradaNdd>.notaValor() = addColumnDouble(NotaEntradaNdd::valorNota) {
    this.setHeader("Valor")
  }
}