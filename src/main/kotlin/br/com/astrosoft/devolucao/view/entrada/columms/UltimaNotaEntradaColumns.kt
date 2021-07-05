package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object UltimaNotaEntradaColumns {
  fun Grid<UltimaNotaEntrada>.notaLoja() = addColumnInt(UltimaNotaEntrada::lj) {
    this.setHeader("Loja")
  }

  fun Grid<UltimaNotaEntrada>.notaNi() = addColumnInt(UltimaNotaEntrada::ni) {
    this.setHeader("NI")
  }

  fun Grid<UltimaNotaEntrada>.notaData() = addColumnLocalDate(UltimaNotaEntrada::data) {
    this.setHeader("Data")
  }

  fun Grid<UltimaNotaEntrada>.notaNfe() = addColumnString(UltimaNotaEntrada::nfe) {
    this.setHeader("Nfe")
  }

  fun Grid<UltimaNotaEntrada>.notaForn() = addColumnString(UltimaNotaEntrada::forn) {
    this.setHeader("Forn")
  }

  fun Grid<UltimaNotaEntrada>.notaProd() = addColumnString(UltimaNotaEntrada::prod) {
    this.setHeader("Prod")
  }

  fun Grid<UltimaNotaEntrada>.notaDescricao() = addColumnString(UltimaNotaEntrada::descricao) {
    this.setHeader("Descrição")
  }

  fun Grid<UltimaNotaEntrada>.notaIcmsn() = addColumnDouble(UltimaNotaEntrada::icmsn) {
    this.setHeader("ICMS N")
  }

  fun Grid<UltimaNotaEntrada>.notaIcmsp() = addColumnDouble(UltimaNotaEntrada::icmsp) {
    this.setHeader("ICMS P")
  }

  fun Grid<UltimaNotaEntrada>.notaIcmsc() = addColumnDouble(UltimaNotaEntrada::icmsd) {
    this.setHeader("ICMS D")
  }

  fun Grid<UltimaNotaEntrada>.notaIpin() = addColumnDouble(UltimaNotaEntrada::ipin) {
    this.setHeader("IPI N")
  }

  fun Grid<UltimaNotaEntrada>.notaIpip() = addColumnDouble(UltimaNotaEntrada::ipip) {
    this.setHeader("IPI P")
  }

  fun Grid<UltimaNotaEntrada>.notaCstn() = addColumnString(UltimaNotaEntrada::cstn) {
    this.setHeader("CST N")
  }

  fun Grid<UltimaNotaEntrada>.notaCstp() = addColumnString(UltimaNotaEntrada::cstp) {
    this.setHeader("CST P")
  }

  fun Grid<UltimaNotaEntrada>.notaMvan() = addColumnDouble(UltimaNotaEntrada::mvan) {
    this.setHeader("MVA N")
  }

  fun Grid<UltimaNotaEntrada>.notaMvap() = addColumnDouble(UltimaNotaEntrada::mvap) {
    this.setHeader("MVA P")
  }

  fun Grid<UltimaNotaEntrada>.notaNcmn() = addColumnString(UltimaNotaEntrada::ncmn) {
    this.setHeader("NCM N")
  }

  fun Grid<UltimaNotaEntrada>.notaNcmp() = addColumnString(UltimaNotaEntrada::ncmp) {
    this.setHeader("NCM P")
  }
}