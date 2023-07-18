package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.right
import com.vaadin.flow.component.grid.Grid

object ProdutosNotaNddColumns {
  fun Grid<ProdutoNotaEntradaNdd>.produtoCodigo() = addColumnString(ProdutoNotaEntradaNdd::codigo) {
    this.setHeader("Código")
    this.isResizable = true
    this.right()
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCodBarra() = addColumnString(ProdutoNotaEntradaNdd::codBarra) {
    this.setHeader("Código de Barras")
    this.isResizable = true
    this.right()
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoDescricao() = addColumnString(ProdutoNotaEntradaNdd::descricao) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoNcm() = addColumnString(ProdutoNotaEntradaNdd::ncm) {
    this.setHeader("NCM")
    this.isResizable = true
    this.right()
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCst() = addColumnString(ProdutoNotaEntradaNdd::cst) {
    this.setHeader("CST")
    this.isResizable = true
    this.right()
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoCfop() = addColumnString(ProdutoNotaEntradaNdd::cfop) {
    this.setHeader("CFOP")
    this.isResizable = true
    this.right()
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoUn() = addColumnString(ProdutoNotaEntradaNdd::un) {
    this.setHeader("UN")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoQuantidade() = addColumnDouble(ProdutoNotaEntradaNdd::quantidade) {
    this.setHeader("Quantidade")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoQuantidadeAvaria() = addColumnDouble(ProdutoNotaEntradaNdd::quantidadeAvaria) {
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

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorTotalAvaria() = addColumnDouble(ProdutoNotaEntradaNdd::valorTotalAvaria) {
    this.setHeader("V. Unit.")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorIPIAvaria() = addColumnDouble(ProdutoNotaEntradaNdd::valorIPIAvaria) {
    this.setHeader("V. IPI")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqOutros() = addColumnDouble(ProdutoNotaEntradaNdd::aliqOutrosAvaria) {
    this.setHeader("% Outros")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorOutrosAvaria() = addColumnDouble(ProdutoNotaEntradaNdd::valorOutros) {
    this.setHeader("V. Outros")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqFrete() = addColumnDouble(ProdutoNotaEntradaNdd::aliqFreteAvaria) {
    this.setHeader("% Frete")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorFrete() = addColumnDouble(ProdutoNotaEntradaNdd::valorFrete) {
    this.setHeader("V. Frete")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoAliqDifICMS() = addColumnDouble(ProdutoNotaEntradaNdd::aliqDifICMS) {
    this.setHeader("% Dif ICMS")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorDifICMS() = addColumnDouble(ProdutoNotaEntradaNdd::valorDifICMS) {
    this.setHeader("V. Dif ICMS")
    this.isResizable = true
  }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorUnitarioAvaria() =
    addColumnDouble(ProdutoNotaEntradaNdd::valorUnitAvaria) {
      this.setHeader("V. Unt Av")
      this.isResizable = true
    }

  fun Grid<ProdutoNotaEntradaNdd>.produtoValorFinalAvaria() =
    addColumnDouble(ProdutoNotaEntradaNdd::valorTotalFinalAvaria) {
      this.setHeader("V. Total Av")
      this.isResizable = true
    }
}