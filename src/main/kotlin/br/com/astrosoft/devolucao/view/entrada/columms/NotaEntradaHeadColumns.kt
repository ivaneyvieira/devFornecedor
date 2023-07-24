package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaHeadList
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.right
import com.vaadin.flow.component.grid.Grid

object NotaEntradaHeadColumns {
  fun Grid<NotaEntradaHeadList>.notaLoja() = addColumnInt(NotaEntradaHeadList::lj) {
    this.setHeader("Loja")
    this.isResizable = true
  }

  fun Grid<NotaEntradaHeadList>.notaNi() = addColumnInt(NotaEntradaHeadList::ni) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NotaEntradaHeadList>.notaData() = addColumnLocalDate(NotaEntradaHeadList::data) {
    this.setHeader("Entrada")
    this.isResizable = true
  }

  fun Grid<NotaEntradaHeadList>.notaDataEmissao() = addColumnLocalDate(NotaEntradaHeadList::dataEmissao) {
    this.setHeader("Emissão")
    this.isResizable = true
  }

  fun Grid<NotaEntradaHeadList>.notaNfe() = addColumnString(NotaEntradaHeadList::nfe) {
    this.setHeader("Nota")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaEntradaHeadList>.notaFornCad() = addColumnString(NotaEntradaHeadList::fornCad) {
    this.setHeader("F Cad")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaEntradaHeadList>.notaFornNota() = addColumnString(NotaEntradaHeadList::fornNota) {
    this.setHeader("For")
    this.isResizable = true
    this.right()
  }
}