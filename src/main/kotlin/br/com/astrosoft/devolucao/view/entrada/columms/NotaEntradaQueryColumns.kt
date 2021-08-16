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
  }

  fun Grid<NotaEntradaQuery>.notaQueryNi() = addColumnInt(NotaEntradaQuery::ni) {
    this.setHeader("NI")
  }

  fun Grid<NotaEntradaQuery>.notaQueryData() = addColumnLocalDate(NotaEntradaQuery::data) {
    this.setHeader("Data")
  }

  fun Grid<NotaEntradaQuery>.notaQueryNfe() = addColumnString(NotaEntradaQuery::nfe) {
    this.setHeader("NF")
  }

  fun Grid<NotaEntradaQuery>.notaQueryFornCad() = addColumnInt(NotaEntradaQuery::fornCad) {
    this.setHeader("F Cad")
  }

  fun Grid<NotaEntradaQuery>.notaQueryFornNota() = addColumnInt(NotaEntradaQuery::fornNota) {
    this.setHeader("F Nota")
  }

  fun Grid<NotaEntradaQuery>.notaQueryProd() = addColumnString(NotaEntradaQuery::prod) {
    this.setHeader("Prod")
  }

  fun Grid<NotaEntradaQuery>.notaQueryDescricao() = addColumnString(NotaEntradaQuery::descricao) {
    this.setHeader("Descrição")
  }

  fun Grid<NotaEntradaQuery>.notaQueryNcm() = addColumnString(NotaEntradaQuery::ncm) {
    this.setHeader("NCM")
  }

  fun Grid<NotaEntradaQuery>.notaQueryCst() = addColumnString(NotaEntradaQuery::cst) {
    this.setHeader("CST")
  }

  fun Grid<NotaEntradaQuery>.notaQueryCfop() = addColumnString(NotaEntradaQuery::cfop) {
    this.setHeader("CFOP")
  }

  fun Grid<NotaEntradaQuery>.notaQueryUn() = addColumnString(NotaEntradaQuery::un) {
    this.setHeader("UN")
  }

  fun Grid<NotaEntradaQuery>.notaQueryQuant() = addColumnInt(NotaEntradaQuery::quant) {
    this.setHeader("Quant")
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorUnit() = addColumnDouble(NotaEntradaQuery::valorUnit) {
    this.setHeader("Valor Unitário")
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorTotal() = addColumnDouble(NotaEntradaQuery::valorTotal) {
    this.setHeader("Valor Total")
  }

  fun Grid<NotaEntradaQuery>.notaQueryBaseIcms() = addColumnDouble(NotaEntradaQuery::baseIcms) {
    this.setHeader("B Calc ICMS")
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorIpi() = addColumnDouble(NotaEntradaQuery::valorIPI) {
    this.setHeader("Valor IPI")
  }

  fun Grid<NotaEntradaQuery>.notaQueryAliqIpi() = addColumnDouble(NotaEntradaQuery::aliqIpi) {
    this.setHeader("Aliq IPI")
  }

  fun Grid<NotaEntradaQuery>.notaQueryValorIcms() = addColumnDouble(NotaEntradaQuery::valorIcms) {
    this.setHeader("Valor ICMS")
  }

  fun Grid<NotaEntradaQuery>.notaQueryAliqIcms() = addColumnDouble(NotaEntradaQuery::aliqIcms) {
    this.setHeader("Aliq ICMS")
  }
}