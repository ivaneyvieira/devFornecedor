package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaXML
import br.com.astrosoft.framework.view.*
import com.vaadin.flow.component.grid.Grid

object NotaEntradaNddViewColumns {
  fun Grid<NotaEntradaXML>.nfeFileNotaNumero() = addColumnString(NotaEntradaXML::notaFiscal) {
    this.setHeader("Número")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaEntradaXML>.nfeFileNotaLoja() = addColumnInt(NotaEntradaXML::loja) {
    this.setHeader("Loja")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileNotaNI() = addColumnInt(NotaEntradaXML::ni) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileCNPJ() = addColumnString(NotaEntradaXML::cnpjEmitente) {
    this.setHeader("CNPJ")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileNomeFornecedor() = addColumnString(NotaEntradaXML::nomeFornecedor) {
    this.setHeader("Fornecedor")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileCfop() = addColumnInt(NotaEntradaXML::cfop) {
    this.setHeader("CFOP")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileChave() = addColumnString(NotaEntradaXML::chave) {
    this.setHeader("Chave")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileEmissao() = addColumnLocalDate(NotaEntradaXML::dataEmissao) {
    this.setHeader("Emissão")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileEntrada() = addColumnLocalDate(NotaEntradaXML::dataEntrada) {
    this.setHeader("Entrada")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileTotal() = addColumnDouble(NotaEntradaXML::valorTotal) {
    this.setHeader("Valor")
    this.isResizable = true
  }

  fun Grid<NotaEntradaXML>.nfeFileValorProduto() = addColumnDouble(NotaEntradaXML::valorTotalProdutos) {
    this.setHeader("Valor Produtos")
    this.isResizable = true
  }
}