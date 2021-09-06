package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object FornecedorSapViewColumns {
  fun Grid<FornecedorSap>.fornecedorCodigoSaci() = addColumnInt(FornecedorSap::vendno) {
    this.setHeader("Código Saci")
  }

  fun Grid<FornecedorSap>.fornecedorCodigoSap() = addColumnInt(FornecedorSap::codigo) {
    this.setHeader("Código Sap")
  }

  fun Grid<FornecedorSap>.fornecedorNome() = addColumnString(FornecedorSap::nome) {
    this.setHeader("Fornecedor")
  }

  fun Grid<FornecedorSap>.fornecedorPrimeiraData() = addColumnLocalDate(FornecedorSap::primeiroData) {
    this.setHeader("Primeira Data")
  }

  fun Grid<FornecedorSap>.fornecedorUltimaData() = addColumnLocalDate(FornecedorSap::ultimaData) {
    this.setHeader("Ultima Data")
  }

  fun Grid<FornecedorSap>.fornecedorSaldoTotal() = addColumnDouble(FornecedorSap::saldoTotal) {
    this.setHeader("Saldo Total")
  }

  fun Grid<FornecedorSap>.fornecedorValorSap() = addColumnDouble(FornecedorSap::totalSap) {
    this.setHeader("V SAP")
  }

  fun Grid<FornecedorSap>.fornecedorValorSaci() = addColumnDouble(FornecedorSap::totalSaci) {
    this.setHeader("V SACI")
  }

  fun Grid<FornecedorSap>.fornecedorValorDiferenca() = addColumnDouble(FornecedorSap::diferenca) {
    this.setHeader("DIF")
  }
}