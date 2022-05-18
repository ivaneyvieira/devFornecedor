package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object FornecedorProdutoColumns {
  fun Grid<FornecedorProduto>.fornecedorFornecedor() = addColumnInt(FornecedorProduto::vendno) {
    this.setHeader("Fornecedor")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorProduto>.fornecedorCliente() = addColumnInt(FornecedorProduto::custno) {
    this.setHeader("Cliente")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorProduto>.fornecedorNome() = addColumnString(FornecedorProduto::nomeFornecedor) {
    this.setHeader("Nome")
  }

  fun Grid<FornecedorProduto>.fornecedorInvno() = addColumnInt(FornecedorProduto::invno) {
    this.setHeader("NI")
  }

  fun Grid<FornecedorProduto>.fornecedorNota() = addColumnString(FornecedorProduto::nota) {
    this.setHeader("NF")
  }

  fun Grid<FornecedorProduto>.fornecedorDataNF() = addColumnLocalDate(FornecedorProduto::data) {
    this.setHeader("Data")
  }
}