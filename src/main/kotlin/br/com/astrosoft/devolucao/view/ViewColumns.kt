package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.FornecedorEntrada
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaEntrada
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnLocalTime
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid
import java.time.LocalDate

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

fun Grid<Fornecedor>.fornecedorUltimaData() = addColumnLocalDate(Fornecedor::ultimaData) {
  this.setHeader("Ultima Data")
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
//*******************************
fun Grid<NFFile>.nfFileDescricao() = addColumnString(NFFile::nome) {
  this.setHeader("Descrição")
}
// ****************************************************************************************
// Emails
// ****************************************************************************************
fun Grid<EmailDB>.emailTipo() = addColumnString(EmailDB::tipoEmail) {
  this.setHeader("Tipo")
}

fun Grid<EmailDB>.emailEmail() = addColumnString(EmailDB::email) {
  this.setHeader("E-mail")
}

fun Grid<EmailDB>.emailAssunto() = addColumnString(EmailDB::assunto) {
  this.setHeader("Assunto")
}

fun Grid<EmailDB>.emailData() = addColumnLocalDate(EmailDB::data) {
  this.setHeader("Data")
}

fun Grid<EmailDB>.emailHora() = addColumnLocalTime(EmailDB::hora) {
  this.setHeader("Hora")
}

// ****************************************************************************************
// Fornecedor Entrada
// ****************************************************************************************
fun Grid<FornecedorEntrada>.fornecedorEntradaNumero() = addColumnInt(FornecedorEntrada::vendno) {
  this.setHeader("Número")
}

fun Grid<FornecedorEntrada>.notaEntradaUltimaData() = addColumnLocalDate(FornecedorEntrada::ultimaData) {
  this.setHeader("Ultima Data")
}

fun Grid<FornecedorEntrada>.fornecedorEntradaNome() = addColumnString(FornecedorEntrada::fornecedor) {
  this.setHeader("Fornecedor")
}

// ****************************************************************************************
// Notas Entrada
// ****************************************************************************************
fun Grid<NotaEntrada>.notaEntradaLoja() = addColumnInt(NotaEntrada::storeno) {
  this.setHeader("Loja")
}

fun Grid<NotaEntrada>.notaEntradaNota() = addColumnString(NotaEntrada::nota) {
  this.setHeader("NF")
}

fun Grid<NotaEntrada>.notaEntradaData() = addColumnLocalDate(NotaEntrada::dataNota) {
  this.setHeader("Data")
}

fun Grid<NotaEntrada>.notaEntradaHora() = addColumnString(NotaEntrada::horaNota) {
  this.setHeader("Loja")
}

fun Grid<NotaEntrada>.notaEntradaNfKey() = addColumnString(NotaEntrada::nfekey) {
  this.setHeader("Chave de acesso")
}

// ****************************************************************************************
// Agenda
// ****************************************************************************************
fun Grid<Agenda>.agendaLoja() = addColumnInt(Agenda::loja) {
  this.setHeader("Loja")
}

fun Grid<Agenda>.agendaData() = addColumnLocalDate(Agenda::data) {
  this.setHeader("Data")
}

fun Grid<Agenda>.agendaHora() = addColumnString(Agenda::hora) {
  this.setHeader("Hora")
}

fun Grid<Agenda>.agendaRecebedor() = addColumnString(Agenda::recebedor) {
  this.setHeader("Recebedor")
}

fun Grid<Agenda>.agendaOrd() = addColumnString(Agenda::invno) {
  this.setHeader("Ord")
}

fun Grid<Agenda>.agendaFornecedor() = addColumnInt(Agenda::fornecedor) {
  this.setHeader("Forn")
}

fun Grid<Agenda>.agendaAbrev() = addColumnString(Agenda::abreviacao) {
  this.setHeader("Abrev")
}

fun Grid<Agenda>.agendaEmissao() = addColumnLocalDate(Agenda::emissao) {
  this.setHeader("Emissão")
}

fun Grid<Agenda>.agendaNf() = addColumnString(Agenda::nf) {
  this.setHeader("NF")
}

fun Grid<Agenda>.agendaVolume() = addColumnString(Agenda::volume) {
  this.setHeader("Valume")
}

fun Grid<Agenda>.agendaTotal() = addColumnDouble(Agenda::total) {
  this.setHeader("Total")
}

fun Grid<Agenda>.agendaTransp() = addColumnInt(Agenda::transp) {
  this.setHeader("Transp")
}

fun Grid<Agenda>.agendaNome() = addColumnString(Agenda::nome) {
  this.setHeader("Nome")
}

fun Grid<Agenda>.agendaPedido() = addColumnInt(Agenda::pedido) {
  this.setHeader("Pedido")
}

