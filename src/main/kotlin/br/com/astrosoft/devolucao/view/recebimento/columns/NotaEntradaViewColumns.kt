package br.com.astrosoft.devolucao.view.recebimento.columns

import br.com.astrosoft.devolucao.model.beans.NotaEntrada
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaEntradaViewColumns {
  fun Grid<NotaEntrada>.notaEntradaLoja() = addColumnInt(NotaEntrada::storeno) {
    this.setHeader("Loja")
  }
  
  fun Grid<NotaEntrada>.notaEntradaNota() = addColumnString(NotaEntrada::nota) {
    this.setHeader("NF")
  }
  
  fun Grid<NotaEntrada>.notaEntradaData() = addColumnLocalDate(NotaEntrada::dataNota) {
    this.setHeader("Data")
  }
  
  fun Grid<NotaEntrada>.notaEntradaHora() = addColumnString(NotaEntrada::horaNota) {
    this.setHeader("Loja")
  }
  
  fun Grid<NotaEntrada>.notaEntradaNfKey() = addColumnString(NotaEntrada::nfekey) {
    this.setHeader("Chave de acesso")
  }
}