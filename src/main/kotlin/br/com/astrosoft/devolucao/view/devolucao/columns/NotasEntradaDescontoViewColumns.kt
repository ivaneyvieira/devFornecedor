package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaEntradaDesconto
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotasEntradaDescontoViewColumns {
  fun Grid<NotaEntradaDesconto>.notaDescontoLoja() = addColumnInt(NotaEntradaDesconto::loja) {
    this.setHeader("Loja")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoNI() = addColumnInt(NotaEntradaDesconto::ni) {
    this.setHeader("NI")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoNumero() = addColumnString(NotaEntradaDesconto::nota) {
    this.setHeader("Nota")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoDataEmissao() = addColumnLocalDate(NotaEntradaDesconto::dataNota) {
    this.setHeader("Emissão")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoDataEntrada() = addColumnLocalDate(NotaEntradaDesconto::dataEntrada) {
    this.setHeader("Entrada")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoDataVencimento() = addColumnLocalDate(NotaEntradaDesconto::vencimento) {
    this.setHeader("Vencimento")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoValorNota() = addColumnDouble(NotaEntradaDesconto::valor) {
    this.setHeader("Valor Nota")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoValorDesconto() = addColumnDouble(NotaEntradaDesconto::desconto) {
    this.setHeader("Valor Desconto")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoValorPago() = addColumnDouble(NotaEntradaDesconto::pagamento) {
    this.setHeader("ValorPago")
  }

  fun Grid<NotaEntradaDesconto>.notaDescontoObs() = addColumnString(NotaEntradaDesconto::obsNota) {
    this.setHeader("Observação")
    this.isAutoWidth = false
    this.setWidth("40em")
  }
}