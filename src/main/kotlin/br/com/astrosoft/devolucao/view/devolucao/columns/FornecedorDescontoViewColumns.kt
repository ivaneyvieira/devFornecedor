package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.FornecedorDesconto
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object FornecedorDescontoViewColumns {
  fun Grid<FornecedorDesconto>.fornecedorDescontoCodigo() = addColumnInt(FornecedorDesconto::vendno) {
    this.setHeader("Código Forn.")
  }

  fun Grid<FornecedorDesconto>.fornecedorDescontoCliente() = addColumnInt(FornecedorDesconto::custno) {
    this.setHeader("Cliente")
  }

  fun Grid<FornecedorDesconto>.fornecedorDescontoNome() = addColumnString(FornecedorDesconto::fornecedor) {
    this.setHeader("Fornecedor")
  }

  fun Grid<FornecedorDesconto>.fornecedorDescontoPrimeiraData() = addColumnLocalDate(FornecedorDesconto::primeiraData) {
    this.setHeader("Primeira Data")
  }

  fun Grid<FornecedorDesconto>.fornecedorDescontoUltimaData() = addColumnLocalDate(FornecedorDesconto::ultimaData) {
    this.setHeader("Ultima Data")
  }


  fun Grid<FornecedorDesconto>.fornecedorDescontoValorTotal() = addColumnDouble(FornecedorDesconto::valorTotal) {
    this.setHeader("Valor Total")
  }
}