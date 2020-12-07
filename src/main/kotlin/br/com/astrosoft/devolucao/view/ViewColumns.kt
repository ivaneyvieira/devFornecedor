package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.Pedido
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosPedido
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

// ****************************************************************************************
// Notas
// ****************************************************************************************
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

fun Grid<NotaSaida>.notaFatura() = addColumnString(NotaSaida::fatura) {
  this.setHeader("Fatura")
}

fun Grid<NotaSaida>.notaDataNota() = addColumnLocalDate(NotaSaida::dataNota) {
  this.setHeader("Data")
}

fun Grid<NotaSaida>.notaValor() = addColumnDouble(NotaSaida::valor) {
  this.setHeader("Valor")
}

// ****************************************************************************************
// Pedidos
// ****************************************************************************************
fun Grid<Pedido>.pedidoLoja() = addColumnInt(Pedido::loja) {
  this.setHeader("Loja")
}

fun Grid<Pedido>.pedidoPedido() = addColumnInt(Pedido::pedido) {
  this.setHeader("Pedido")
}

fun Grid<Pedido>.pedidoDataPedido() = addColumnLocalDate(Pedido::dataPedido) {
  this.setHeader("Data")
}

fun Grid<Pedido>.pedidoNota() = addColumnString(Pedido::nota) {
  this.setHeader("Nota")
}

fun Grid<Pedido>.pedidoFatura() = addColumnString(Pedido::fatura) {
  this.setHeader("Fatura")
}

fun Grid<Pedido>.pedidoDataNota() = addColumnLocalDate(Pedido::dataNota) {
  this.setHeader("Data")
}

fun Grid<Pedido>.pedidoValor() = addColumnDouble(Pedido::valor) {
  this.setHeader("Valor")
}

// ****************************************************************************************
// Fornecedor
// ****************************************************************************************
fun Grid<Fornecedor>.fornecedorCodigo() = addColumnInt(Fornecedor::vendno) {
  this.setHeader("Fornecedor")
}

fun Grid<Fornecedor>.fornecedorCliente() = addColumnInt(Fornecedor::custno) {
  this.setHeader("Cliente")
}

fun Grid<Fornecedor>.fornecedorNome() = addColumnString(Fornecedor::fornecedor) {
  this.setHeader("Fornecedor")
}

// ****************************************************************************************
// Representantes
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

// ****************************************************************************************
// produtos de pedidos
// ****************************************************************************************
fun Grid<ProdutosPedido>.produtoPedLoja() = addColumnInt(ProdutosPedido::loja) {
  this.setHeader("Loja")
}

fun Grid<ProdutosPedido>.produtoPedNumero() = addColumnInt(ProdutosPedido::pedido) {
  this.setHeader("Pedido")
}

fun Grid<ProdutosPedido>.produtoPedCodigo() = addColumnString(ProdutosPedido::codigo) {
  this.setHeader("Código")
}

fun Grid<ProdutosPedido>.produtoPedDescricao() = addColumnString(ProdutosPedido::descricao) {
  this.setHeader("Descrição")
}

fun Grid<ProdutosPedido>.produtoPedGrade() = addColumnString(ProdutosPedido::grade) {
  this.setHeader("Grade")
}

fun Grid<ProdutosPedido>.produtoPedBarcode() = addColumnString(ProdutosPedido::barcode) {
  this.setHeader("Código de Barras")
}

fun Grid<ProdutosPedido>.produtoPedUn() = addColumnString(ProdutosPedido::un) {
  this.setHeader("Un")
}

fun Grid<ProdutosPedido>.produtoPedQtde() = addColumnInt(ProdutosPedido::qtde) {
  this.setHeader("Qtde")
}

fun Grid<ProdutosPedido>.produtoPedValorUnitario() = addColumnDouble(ProdutosPedido::valorUnitario) {
  this.setHeader("R$ Unit")
}

fun Grid<ProdutosPedido>.produtoPedValortotal() = addColumnDouble(ProdutosPedido::valorTotal) {
  this.setHeader("R$ Total")
}

//*******************************
fun Grid<NFFile>.nfFileDescricao() = addColumnString(NFFile::nome) {
  this.setHeader("Descrição")
}
