package br.com.astrosoft.devolucao.view.saida.columns

import br.com.astrosoft.devolucao.model.beans.ReimpressaoNota
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object ReimpressaoColumns {
  fun Grid<ReimpressaoNota>.dataNotaSaida() = addColumnLocalDate(ReimpressaoNota::data) {
    this.setHeader("Data")
  }

  fun Grid<ReimpressaoNota>.horaNotaSaida() = addColumnString(ReimpressaoNota::hora) {
    this.setHeader("Hora")
  }

  fun Grid<ReimpressaoNota>.lojaNotaSaida() = addColumnInt(ReimpressaoNota::loja) {
    this.setHeader("Loja")
  }

  fun Grid<ReimpressaoNota>.numeroNotaSaida() = addColumnString(ReimpressaoNota::nota) {
    this.setHeader("Nota")
  }

  fun Grid<ReimpressaoNota>.tipoNotaSaida() = addColumnString(ReimpressaoNota::tipo) {
    this.setHeader("Tipo")
  }

  fun Grid<ReimpressaoNota>.usuarioNotaSaida() = addColumnString(ReimpressaoNota::usuario) {
    this.setHeader("Usuário")
  }
}