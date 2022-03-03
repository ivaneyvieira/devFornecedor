package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.FornecedorNdd
import br.com.astrosoft.framework.view.*
import com.vaadin.flow.component.grid.Grid

object FornecedorNddViewColumns {
  fun Grid<FornecedorNdd>.fornecedorCodigoSaci() = addColumnInt(FornecedorNdd::vendno) {
    this.setHeader("Código Saci")
  }

  fun Grid<FornecedorNdd>.fornecedorNome() = addColumnString(FornecedorNdd::nome) {
    this.setHeader("Fornecedor")
  }

  fun Grid<FornecedorNdd>.fornecedorPrimeiraData() = addColumnLocalDate(FornecedorNdd::primeiraData) {
    this.setHeader("Primeira Data")
  }

  fun Grid<FornecedorNdd>.fornecedorUltimaData() = addColumnLocalDate(FornecedorNdd::ultimaData) {
    this.setHeader("Ultima Data")
  }

  fun Grid<FornecedorNdd>.fornecedorTemIPI() = addColumnBool(FornecedorNdd::temIPI) {
    this.setHeader("Tem IPI")
  }

  fun Grid<FornecedorNdd>.fornecedorSaldoTotal() = addColumnDouble(FornecedorNdd::valorTotal) {
    this.setHeader("Saldo Total")
  }
}