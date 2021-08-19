package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaSaidaViewColumns {
  fun Grid<NotaSaida>.notaLoja() = addColumnInt(NotaSaida::loja) {
    this.setHeader("Loja")
  }

  fun Grid<NotaSaida>.notaPdv() = addColumnInt(NotaSaida::pdv) {
    this.setHeader("Pdv")
  }

  fun Grid<NotaSaida>.notaTransacao() = addColumnInt(NotaSaida::transacao) {
    this.setHeader("Transacao")
  }

  fun Grid<NotaSaida>.notaPedido() = addColumnInt(NotaSaida::pedido) {
    this.setHeader("Pedido")
  }

  fun Grid<NotaSaida>.notaDataPedido() = addColumnLocalDate(NotaSaida::dataPedido) {
    this.setHeader("Data")
  }

  fun Grid<NotaSaida>.notaNota() = addColumnString(NotaSaida::nota) {
    this.setHeader("Nota")
  }

  fun Grid<NotaSaida>.tipoPagDesconto() = addColumnString(NotaSaida::tipoPag) {
    this.setHeader("Obs Pgto1")
    this.setClassNameGenerator { "marcaDiferenca" }
  }

  fun Grid<NotaSaida>.documentoPagDesconto() = addColumnString(NotaSaida::documentoPag) {
    this.setHeader("Obs Pgto2")
    this.setClassNameGenerator { "marcaDiferenca" }
  }

  fun Grid<NotaSaida>.niPagDesconto() = addColumnString(NotaSaida::niPag) {
    this.setHeader("NI")
    this.setClassNameGenerator { "marcaDiferenca" }
  }

  fun Grid<NotaSaida>.vencimentoPagDesconto() = addColumnString(NotaSaida::vencimentoPag) {
    this.setHeader("Vencimento")
    this.setClassNameGenerator { "marcaDiferenca" }
  }

  fun Grid<NotaSaida>.notaFatura() = addColumnString(NotaSaida::fatura) {
    this.setHeader("Fatura")
  }

  fun Grid<NotaSaida>.notaObservacao() = addColumnString(NotaSaida::remarks) {
    this.setHeader("Obs")
  }

  fun Grid<NotaSaida>.notaDataNota() = addColumnLocalDate(NotaSaida::dataNota) {
    this.setHeader("Data")
  }

  fun Grid<NotaSaida>.notaValor() = addColumnDouble(NotaSaida::valorNota) {
    this.setHeader("Valor")
  }
}
