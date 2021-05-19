package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.Parcela
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object ParcelaViewColumns {
  fun Grid<Parcela>.parcelaNi() = addColumnInt(Parcela::ni) {
    this.setHeader("NI")
  }

  fun Grid<Parcela>.parcelaNota() = addColumnString(Parcela::nota) {
    this.setHeader("Nota")
  }

  fun Grid<Parcela>.parcelaVencimento() = addColumnLocalDate(Parcela::dtVencimento) {
    this.setHeader("Vencimento")
  }

  fun Grid<Parcela>.parcelaValor() = addColumnDouble(Parcela::valor) {
    this.setHeader("Valor")
  }
}