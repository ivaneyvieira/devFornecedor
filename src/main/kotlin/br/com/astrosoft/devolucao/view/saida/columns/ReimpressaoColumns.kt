package br.com.astrosoft.devolucao.view.saida.columns

import br.com.astrosoft.devolucao.model.beans.ReimpressaoNota
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid
import java.time.LocalDate

object ReimpressaoColumns {
  fun Grid<ReimpressaoNota>.dataReimpressao() = addColumnLocalDate(ReimpressaoNota::data) {
    this.setHeader("Data Reimp")
  }

  fun Grid<ReimpressaoNota>.horaReimpressao() = addColumnString(ReimpressaoNota::hora) {
    this.setHeader("Hora Reimp")
  }

  fun Grid<ReimpressaoNota>.lojaReimpressao() = addColumnInt(ReimpressaoNota::loja) {
    this.setHeader("Loja")
  }

  fun Grid<ReimpressaoNota>.numeroReimpressao() = addColumnString(ReimpressaoNota::nota) {
    this.setHeader("NF")
  }

  fun Grid<ReimpressaoNota>.tipoReimpressao() = addColumnString(ReimpressaoNota::tipo) {
    this.setHeader("Tipo")
  }

  fun Grid<ReimpressaoNota>.usuarioReimpressao() = addColumnString(ReimpressaoNota::usuario) {
    this.setHeader("Usuário")
  }

  fun Grid<ReimpressaoNota>.dataNotaReimpressao() = addColumnLocalDate(ReimpressaoNota::dataNota) {
    this.setHeader("Data")
  }

  fun Grid<ReimpressaoNota>.codCliReimpressao() = addColumnInt(ReimpressaoNota::codcli) {
    this.setHeader("Cód Cli")
  }

  fun Grid<ReimpressaoNota>.nomecliReimpressao() = addColumnString(ReimpressaoNota::nomecli) {
    this.setHeader("Nome Cliente")
  }

  fun Grid<ReimpressaoNota>.valorReimpressao() = addColumnDouble(ReimpressaoNota::valor) {
    this.setHeader("Valor")
  }
}