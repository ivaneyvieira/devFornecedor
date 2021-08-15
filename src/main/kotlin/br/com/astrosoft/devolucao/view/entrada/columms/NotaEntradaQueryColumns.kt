package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaEntradaQueryColumns {
  fun Grid<NotaEntradaQuery>.notaQueryLoja() = addColumnInt(NotaEntradaQuery::lj) {
    this.setHeader("Loja")
  }

  fun Grid<NotaEntradaQuery>.notaQueryNi() = addColumnInt(NotaEntradaQuery::ni) {
    this.setHeader("NI")
  }

  fun Grid<NotaEntradaQuery>.notaQueryData() = addColumnLocalDate(NotaEntradaQuery::data) {
    this.setHeader("Data")
  }

  fun Grid<NotaEntradaQuery>.notaQueryNfe() = addColumnString(NotaEntradaQuery::nfe) {
    this.setHeader("Nfe")
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

  fun Grid<NotaEntradaQuery>.notaQueryIcmsn() = addColumnDouble(NotaEntradaQuery::icmsn) {
    this.setHeader("ICMS N")
  }

  fun Grid<NotaEntradaQuery>.notaQueryIcmsp() = addColumnDouble(NotaEntradaQuery::icmsnv) {
    this.setHeader("ICMS R$")
  }

  fun Grid<NotaEntradaQuery>.notaQueryIcmsr() = addColumnDouble(NotaEntradaQuery::icmsc) {
    this.setHeader("ICMS R")
  }

  fun Grid<NotaEntradaQuery>.notaQueryRedIcms() = addColumnDouble(NotaEntradaQuery::icmsd) {
    this.setHeader("Red %")
  }

  fun Grid<NotaEntradaQuery>.notaQueryIpin() = addColumnDouble(NotaEntradaQuery::ipin) {
    this.setHeader("IPI N")
  }

  fun Grid<NotaEntradaQuery>.notaQueryIpip() = addColumnDouble(NotaEntradaQuery::ipinv) {
    this.setHeader("IPI R$")
  }

  fun Grid<NotaEntradaQuery>.notaQueryCstn() = addColumnString(NotaEntradaQuery::cstn) {
    this.setHeader("CST N")
  }

  fun Grid<NotaEntradaQuery>.notaQueryMvan() = addColumnDouble(NotaEntradaQuery::mvan) {
    this.setHeader("MVA N")
  }

  fun Grid<NotaEntradaQuery>.notaQueryMvap() = addColumnDouble(NotaEntradaQuery::mvanv) {
    this.setHeader("MVA R$")
  }
}