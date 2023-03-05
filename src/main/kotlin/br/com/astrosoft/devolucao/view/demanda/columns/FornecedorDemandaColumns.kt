package br.com.astrosoft.devolucao.view.demanda.columns

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object FornecedorDemandaColumns {
  fun Grid<FornecedorProduto>.fornecedorDemandaFornecedor() = addColumnInt(FornecedorProduto::vendno) {
    this.setHeader("Fornecedor")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaCliente() = addColumnInt(FornecedorProduto::custno) {
    this.setHeader("Cliente")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaNome() = addColumnString(FornecedorProduto::nomeFornecedor) {
    this.setHeader("Nome")
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaFantasia() = addColumnString(FornecedorProduto::nomeFantasia) {
    this.setHeader("Nome Fantasia")
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaCNPJ() = addColumnString(FornecedorProduto::cnpj) {
    this.setHeader("CNPJ")
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaUF() = addColumnString(FornecedorProduto::uf) {
    this.setHeader("UF")
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaInvno() = addColumnInt(FornecedorProduto::invno) {
    this.setHeader("NI")
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaNota() = addColumnString(FornecedorProduto::nota) {
    this.setHeader("NF")
  }

  fun Grid<FornecedorProduto>.fornecedorDemandaDataNF() = addColumnLocalDate(FornecedorProduto::data) {
    this.setHeader("Data")
  }
}