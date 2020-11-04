package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

//Notaas de devolução
fun Grid<NotaDevolucao>.notaLoja() = addColumnInt(NotaDevolucao::loja) {
  this.setHeader("Loja")
}

fun Grid<NotaDevolucao>.notaPdv() = addColumnInt(NotaDevolucao::pdv) {
  this.setHeader("Pdv")
}

fun Grid<NotaDevolucao>.notaTransacao() = addColumnInt(NotaDevolucao::transacao) {
  this.setHeader("Transacao")
}

fun Grid<NotaDevolucao>.notaPedido() = addColumnInt(NotaDevolucao::pedido) {
  this.setHeader("Pedido")
}

fun Grid<NotaDevolucao>.notaDataPedido() = addColumnLocalDate(NotaDevolucao::dataPedido) {
  this.setHeader("Data")
}

fun Grid<NotaDevolucao>.notaNota() = addColumnString(NotaDevolucao::nota) {
  this.setHeader("Nota")
}

fun Grid<NotaDevolucao>.notaDataNota() = addColumnLocalDate(NotaDevolucao::dataNota) {
  this.setHeader("Data")
}

fun Grid<NotaDevolucao>.notaFornecedor() = addColumnString(NotaDevolucao::fornecedor) {
  this.setHeader("Fornecedor")
}
// ****************************************************************************************
// produtos de nota de saida
// ****************************************************************************************
fun Grid<Representante>.notaRepresentante() = addColumnString(Representante::nome) {
  this.setHeader("Representante")
}

fun Grid<Representante>.notaTelefone() = addColumnString(Representante::telefone) {
  this.setHeader("Telefone")
}

fun Grid<Representante>.notaEmail() = addColumnString(Representante::email) {
  this.setHeader("E-mail")
}

// ****************************************************************************************
// produtos de nota de saida
// ****************************************************************************************
fun Grid<ProdutosNotaSaida>.pedidoLoja() = addColumnInt(ProdutosNotaSaida::loja) {
  this.setHeader("Loja")
}

fun Grid<ProdutosNotaSaida>.pedidoPdv() = addColumnInt(ProdutosNotaSaida::pdv) {
  this.setHeader("Pdv")
}

fun Grid<ProdutosNotaSaida>.pedidoTransacao() = addColumnInt(ProdutosNotaSaida::transacao) {
  this.setHeader("Transacao")
}

fun Grid<ProdutosNotaSaida>.pedidoCodigo() = addColumnString(ProdutosNotaSaida::codigo) {
  this.setHeader("Código")
}

fun Grid<ProdutosNotaSaida>.pedidoDescricao() = addColumnString(ProdutosNotaSaida::descricao) {
  this.setHeader("Descrição")
}

fun Grid<ProdutosNotaSaida>.pedidoGrade() = addColumnString(ProdutosNotaSaida::grade) {
  this.setHeader("Grade")
}

fun Grid<ProdutosNotaSaida>.pedidoQtde() = addColumnInt(ProdutosNotaSaida::qtde) {
  this.setHeader("Qtde")
}
