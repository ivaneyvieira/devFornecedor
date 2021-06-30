package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object ProdutosNotaNddColumns {
  fun Grid<ProdutoNotaEntradaNdd>.produtoCodigo() = addColumnString(ProdutoNotaEntradaNdd::codigo) {
    this.setHeader("Código")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCodBarra() = addColumnString(ProdutoNotaEntradaNdd::codBarra) {
    this.setHeader("Código de Barras")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoDescricao() = addColumnString(ProdutoNotaEntradaNdd::descricao) {
    this.setHeader("Descrição")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoNcm() = addColumnString(ProdutoNotaEntradaNdd::ncm) {
    this.setHeader("NCM")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCst() = addColumnString(ProdutoNotaEntradaNdd::cst) {
    this.setHeader("CST")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCfop() = addColumnString(ProdutoNotaEntradaNdd::cfop) {
    this.setHeader("CFOP")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoUn() = addColumnString(ProdutoNotaEntradaNdd::un) {
    this.setHeader("UN")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoQuantidade() = addColumnDouble(ProdutoNotaEntradaNdd::quantidade) {
    this.setHeader("Quantidade")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorUnitario() = addColumnDouble(ProdutoNotaEntradaNdd::valorUnitario) {
    this.setHeader("Valor Unitário")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorTotal() = addColumnDouble(ProdutoNotaEntradaNdd::valorTotal) {
    this.setHeader("Valor Total")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoBaseIcms() = addColumnDouble(ProdutoNotaEntradaNdd::baseICMS) {
    this.setHeader("B. Calc ICMS")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorIpi() = addColumnDouble(ProdutoNotaEntradaNdd::valorIPI) {
    this.setHeader("Valor IPI")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqIPI() = addColumnDouble(ProdutoNotaEntradaNdd::aliqIPI) {
    this.setHeader("Alíq IPI")
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqICMS() = addColumnDouble(ProdutoNotaEntradaNdd::aliqICMS) {
    this.setHeader("Alíq ICMS")
  }
}