package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaXML
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object NotaXMLColumns {
  fun Grid<NotaXML>.notaLoja() = addColumnInt(NotaXML::lj) {
    this.setHeader("Lj")
    this.isResizable = true
    this.isExpand = false
  }

  fun Grid<NotaXML>.notaNi() = addColumnInt(NotaXML::ni) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaData() = addColumnLocalDate(NotaXML::data, formatPattern = "dd/MM/yy") {
    this.setHeader("Entrada")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaDataEmissao() = addColumnLocalDate(NotaXML::dataEmissao) {
    this.setHeader("Emissão")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaNfe() = addColumnString(NotaXML::nfe) {
    this.setHeader("Nota")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaXML>.notaFornNota() = addColumnString(NotaXML::fornNota) {
    this.setHeader("For NF")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaXML>.notaCstx() = addColumnString(NotaXML::cstx) {
    this.setHeader("CST X")
    this.isResizable = true
    this.isExpand = false
    this.right()
  }

  fun Grid<NotaXML>.notaMvax() = addColumnDouble(NotaXML::mvax) {
    this.setHeader("MVA X")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaBarcodex() = addColumnString(NotaXML::barcodex) {
    this.setHeader("Cód Barras XML")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaXML>.notaRefPrdx() = addColumnString(NotaXML::refPrdx) {
    this.setHeader("Ref X")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaCFOPX() = addColumnString(NotaXML::cfopx) {
    this.setHeader("CFOP X")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaXML>.notaAlIcmsx() = addColumnDouble(NotaXML::alIcmsx) {
    this.setHeader("V ICMS")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaAlPisx() = addColumnDouble(NotaXML::alPisx) {
    this.setHeader("V PIS")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaAlCofinsx() = addColumnDouble(NotaXML::alCofinsx) {
    this.setHeader("V Cofins")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaCodigo() = addColumnString(NotaXML::codigo) {
    this.setHeader("Cód")
    this.isResizable = true
    this.right()
  }

  fun Grid<NotaXML>.notaDescricaox() = addColumnString(NotaXML::descricaox) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaUnidadex() = addColumnString(NotaXML::unidadex) {
    this.setHeader("Unid")
    this.center()
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaUnidadeSaci() = addColumnString(NotaXML::unidadeSaci) {
    this.setHeader("Ud Saci")
    this.center()
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaQuantidade() = addColumnDouble(NotaXML::quant, pattern = "#,##0.####") {
    this.setHeader("Qtd XML")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaQuantidadeSaci() = addColumnInt(NotaXML::quantSaci) {
    this.setHeader("Qtd Saci")
    this.isResizable = true
  }

  fun Grid<NotaXML>.notaAlIpix() = addColumnDouble(NotaXML::alIpix) {
    this.setHeader("V IPI")
    this.isResizable = true
  }
}