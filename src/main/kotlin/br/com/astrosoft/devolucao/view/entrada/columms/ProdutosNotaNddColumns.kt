package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object ProdutosNotaNddColumns {
  fun Grid<ProdutoNotaEntradaNdd>.produtoCodigo() = addColumnString(ProdutoNotaEntradaNdd::codigo) {
    this.setHeader("Código")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCodBarra() = addColumnString(ProdutoNotaEntradaNdd::codBarra) {
    this.setHeader("Código de Barras")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoDescricao() = addColumnString(ProdutoNotaEntradaNdd::descricao) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoNcm() = addColumnString(ProdutoNotaEntradaNdd::ncm) {
    this.setHeader("NCM")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCst() = addColumnString(ProdutoNotaEntradaNdd::cst) {
    this.setHeader("CST")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCfop() = addColumnString(ProdutoNotaEntradaNdd::cfop) {
    this.setHeader("CFOP")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoUn() = addColumnString(ProdutoNotaEntradaNdd::un) {
    this.setHeader("UN")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoQuantidade() = addColumnDouble(ProdutoNotaEntradaNdd::quantidade) {
    this.setHeader("Quantidade")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorUnitario() = addColumnDouble(ProdutoNotaEntradaNdd::valorUnitario) {
    this.setHeader("Valor Unitário")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorTotal() = addColumnDouble(ProdutoNotaEntradaNdd::valorTotal) {
    this.setHeader("Valor Total")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoBaseIcms() = addColumnDouble(ProdutoNotaEntradaNdd::baseICMS) {
    this.setHeader("B. Calc ICMS")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorIpi() = addColumnDouble(ProdutoNotaEntradaNdd::valorIPI) {
    this.setHeader("Valor IPI")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqIPI() = addColumnDouble(ProdutoNotaEntradaNdd::aliqIPI) {
    this.setHeader("Alíq IPI")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqICMS() = addColumnDouble(ProdutoNotaEntradaNdd::aliqICMS) {
    this.setHeader("Alíq ICMS")
    this.isResizable = true
  }
}