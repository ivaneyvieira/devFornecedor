package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaEntradaQueryColumns {
  fun Grid<NotaEntradaQuery>.notaQueryLoja() = addColumnInt(NotaEntradaQuery::lj) {
    this.setHeader("Lj")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryNi() = addColumnInt(NotaEntradaQuery::ni) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryData() = addColumnLocalDate(NotaEntradaQuery::data) {
    this.setHeader("Data")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryNfe() = addColumnString(NotaEntradaQuery::nfe) {
    this.setHeader("NF")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryFornCad() = addColumnInt(NotaEntradaQuery::fornCad) {
    this.setHeader("F Cad")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryFornNota() = addColumnInt(NotaEntradaQuery::fornNota) {
    this.setHeader("F Nota")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryProd() = addColumnString(NotaEntradaQuery::prod) {
    this.setHeader("Prod")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryDescricao() = addColumnString(NotaEntradaQuery::descricao) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryNcm() = addColumnString(NotaEntradaQuery::ncm) {
    this.setHeader("NCM")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryCst() = addColumnString(NotaEntradaQuery::cstn) {
    this.setHeader("CST")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryCfop() = addColumnString(NotaEntradaQuery::cfop) {
    this.setHeader("CFOP")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryUn() = addColumnString(NotaEntradaQuery::un) {
    this.setHeader("UN")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryQuant() = addColumnInt(NotaEntradaQuery::quant) {
    this.setHeader("Quant")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorUnit() = addColumnDouble(NotaEntradaQuery::valorUnit) {
    this.setHeader("Valor Unitário")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorTotal() = addColumnDouble(NotaEntradaQuery::valorTotal) {
    this.setHeader("Valor Total")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryBaseIcms() = addColumnDouble(NotaEntradaQuery::baseIcms) {
    this.setHeader("B Calc ICMS")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorIpi() = addColumnDouble(NotaEntradaQuery::valorIPI) {
    this.setHeader("Valor IPI")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryAliqIpi() = addColumnDouble(NotaEntradaQuery::aliqIpi) {
    this.setHeader("Aliq IPI")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorIcms() = addColumnDouble(NotaEntradaQuery::valorIcms) {
    this.setHeader("Valor ICMS")
    this.isResizable = true
  }

  fun Grid<NotaEntradaQuery>.notaQueryAliqIcms() = addColumnDouble(NotaEntradaQuery::aliqIcms) {
    this.setHeader("Aliq ICMS")
    this.isResizable = true
  }
}