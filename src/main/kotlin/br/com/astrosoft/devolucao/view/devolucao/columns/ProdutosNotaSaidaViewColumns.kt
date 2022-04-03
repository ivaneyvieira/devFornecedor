package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object ProdutosNotaSaidaViewColumns {
  fun Grid<ProdutosNotaSaida>.produtoLoja() = addColumnInt(ProdutosNotaSaida::loja) {
    this.setHeader("Loja")
  }

  fun Grid<ProdutosNotaSaida>.produtoPdv() = addColumnInt(ProdutosNotaSaida::pdv) {
    this.setHeader("Pdv")
  }

  fun Grid<ProdutosNotaSaida>.produtoTransacao() = addColumnInt(ProdutosNotaSaida::transacao) {
    this.setHeader("Transacao")
  }

  fun Grid<ProdutosNotaSaida>.produtoCodigo() = addColumnInt(ProdutosNotaSaida::codigo) {
    this.setHeader("Código")
  }

  fun Grid<ProdutosNotaSaida>.produtoDescricao() = addColumnString(ProdutosNotaSaida::descricao) {
    this.setHeader("Descrição")
  }

  fun Grid<ProdutosNotaSaida>.produtoGrade() = addColumnString(ProdutosNotaSaida::grade) {
    this.setHeader("Grade")
  }

  fun Grid<ProdutosNotaSaida>.produtoQtde() = addColumnInt(ProdutosNotaSaida::qtde) {
    this.setHeader("Qtde")
  }
}