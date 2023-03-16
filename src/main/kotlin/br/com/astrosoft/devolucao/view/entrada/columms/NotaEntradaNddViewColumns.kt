package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaFileXML
import br.com.astrosoft.framework.view.*
import com.vaadin.flow.component.grid.Grid

object NotaEntradaNddViewColumns {
  fun Grid<NotaEntradaFileXML>.nfeFileNotaSaci() = addColumnString(NotaEntradaFileXML::notaFiscal) {
    this.setHeader("Nota Saci")
    this.isResizable = true
  }

  fun Grid<NotaEntradaFileXML>.nfeFileCNPJ() = addColumnString(NotaEntradaFileXML::cnpjEmitente) {
    this.setHeader("CNPJ")
    this.isResizable = true
  }

  fun Grid<NotaEntradaFileXML>.nfeFileNomeFornecedor() = addColumnString(NotaEntradaFileXML::nomeFornecedor) {
    this.setHeader("Fornecedor")
    this.isResizable = true
  }

  fun Grid<NotaEntradaFileXML>.nfeFileChave() = addColumnString(NotaEntradaFileXML::chave) {
    this.setHeader("Chave")
    this.isResizable = true
  }

  fun Grid<NotaEntradaFileXML>.nfeFileData() = addColumnLocalDate(NotaEntradaFileXML::dataEmissao) {
    this.setHeader("Data")
    this.isResizable = true
  }


  fun Grid<NotaEntradaFileXML>.nfeFileTotal() = addColumnDouble(NotaEntradaFileXML::baseCalculoIcms) {
    this.setHeader("Valor")
    this.isResizable = true
  }

  fun Grid<NotaEntradaFileXML>.nfeFileValorProduto() = addColumnDouble(NotaEntradaFileXML::valorTotalProdutos) {
    this.setHeader("Valor Produtos")
    this.isResizable = true
  }
}