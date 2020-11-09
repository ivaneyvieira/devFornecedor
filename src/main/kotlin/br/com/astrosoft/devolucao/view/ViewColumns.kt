package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

//Notaas de devolução
fun Grid<NotaSaida>.notaLoja() = addColumnInt(NotaSaida::loja) {
  this.setHeader("Loja")
}

fun Grid<NotaSaida>.notaPdv() = addColumnInt(NotaSaida::pdv) {
  this.setHeader("Pdv")
}

fun Grid<NotaSaida>.notaTransacao() = addColumnInt(NotaSaida::transacao) {
  this.setHeader("Transacao")
}

fun Grid<NotaSaida>.notaPedido() = addColumnInt(NotaSaida::pedido) {
  this.setHeader("Pedido")
}

fun Grid<NotaSaida>.notaDataPedido() = addColumnLocalDate(NotaSaida::dataPedido) {
  this.setHeader("Data")
}

fun Grid<NotaSaida>.notaNota() = addColumnString(NotaSaida::nota) {
  this.setHeader("Nota")
}

fun Grid<NotaSaida>.notaDataNota() = addColumnLocalDate(NotaSaida::dataNota) {
  this.setHeader("Data")
}

fun Grid<NotaSaida>.notaFornecedor() = addColumnString(NotaSaida::fornecedor) {
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

fun Grid<Representante>.notaCelular() = addColumnString(Representante::celular) {
  this.setHeader("Celular")
}

fun Grid<Representante>.notaEmail() = addColumnString(Representante::email) {
  this.setHeader("E-mail")
}

// ****************************************************************************************
// produtos de nota de saida
// ****************************************************************************************
fun Grid<ProdutosNotaSaida>.produtoLoja() = addColumnInt(ProdutosNotaSaida::loja) {
  this.setHeader("Loja")
}

fun Grid<ProdutosNotaSaida>.produtoPdv() = addColumnInt(ProdutosNotaSaida::pdv) {
  this.setHeader("Pdv")
}

fun Grid<ProdutosNotaSaida>.produtoTransacao() = addColumnInt(ProdutosNotaSaida::transacao) {
  this.setHeader("Transacao")
}

fun Grid<ProdutosNotaSaida>.produtoCodigo() = addColumnString(ProdutosNotaSaida::codigo) {
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

fun Grid<ProdutosNotaSaida>.produtoUltNi() = addColumnInt(ProdutosNotaSaida::ni) {
  this.setHeader("NI")
}

fun Grid<ProdutosNotaSaida>.produtoUltNumero() = addColumnString(ProdutosNotaSaida::numeroNota) {
  this.setHeader("Número")
}

fun Grid<ProdutosNotaSaida>.produtoUltData() = addColumnLocalDate(ProdutosNotaSaida::dataNota) {
  this.setHeader("Data")
}

fun Grid<ProdutosNotaSaida>.produtoUltQtd() = addColumnInt(ProdutosNotaSaida::qttdNota) {
  this.setHeader("Quant")
}
