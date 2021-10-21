package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object UltimaNotaEntradaColumns {
  fun Grid<NfPrecEntrada>.notaLoja() = addColumnInt(NfPrecEntrada::lj) {
    this.setHeader("Loja")
  }

  fun Grid<NfPrecEntrada>.notaNi() = addColumnInt(NfPrecEntrada::ni) {
    this.setHeader("NI")
  }

  fun Grid<NfPrecEntrada>.notaData() = addColumnLocalDate(NfPrecEntrada::data) {
    this.setHeader("Data")
  }

  fun Grid<NfPrecEntrada>.notaNfe() = addColumnString(NfPrecEntrada::nfe) {
    this.setHeader("Nfe")
  }

  fun Grid<NfPrecEntrada>.notaFornCad() = addColumnString(NfPrecEntrada::fornCad) {
    this.setHeader("F Cad")
  }

  fun Grid<NfPrecEntrada>.notaFornNota() = addColumnString(NfPrecEntrada::fornNota) {
    this.setHeader("F Nota")
  }

  fun Grid<NfPrecEntrada>.notaProd() = addColumnString(NfPrecEntrada::prod) {
    this.setHeader("Prod")
  }

  fun Grid<NfPrecEntrada>.notaDescricao() = addColumnString(NfPrecEntrada::descricao) {
    this.setHeader("Descrição")
  }

  fun Grid<NfPrecEntrada>.notaGrade() = addColumnString(NfPrecEntrada::grade) {
    this.setHeader("Grade")
  }

  fun Grid<NfPrecEntrada>.notaIcmsn() = addColumnDouble(NfPrecEntrada::icmsn) {
    this.setHeader("ICMS N")
  }

  fun Grid<NfPrecEntrada>.notaIcmsp() = addColumnDouble(NfPrecEntrada::icmsp) {
    this.setHeader("ICMS P")
  }

  fun Grid<NfPrecEntrada>.notaIcmsr() = addColumnDouble(NfPrecEntrada::icmsc) {
    this.setHeader("ICMS R")
  }

  fun Grid<NfPrecEntrada>.notaRedIcms() = addColumnDouble(NfPrecEntrada::icmsd) {
    this.setHeader("Red %")
  }

  fun Grid<NfPrecEntrada>.notaIpin() = addColumnDouble(NfPrecEntrada::ipin) {
    this.setHeader("IPI N")
  }

  fun Grid<NfPrecEntrada>.notaIpip() = addColumnDouble(NfPrecEntrada::ipip) {
    this.setHeader("IPI P")
  }

  fun Grid<NfPrecEntrada>.notaCstn() = addColumnString(NfPrecEntrada::cstn) {
    this.setHeader("CST N")
  }

  fun Grid<NfPrecEntrada>.notaCstp() = addColumnString(NfPrecEntrada::cstp) {
    this.setHeader("CST P")
  }

  fun Grid<NfPrecEntrada>.notaMvan() = addColumnDouble(NfPrecEntrada::mvan) {
    this.setHeader("MVA N")
  }

  fun Grid<NfPrecEntrada>.notaMvap() = addColumnDouble(NfPrecEntrada::mvap) {
    this.setHeader("MVA P")
  }

  fun Grid<NfPrecEntrada>.notaBarcodep() = addColumnString(NfPrecEntrada::barcodep) {
    this.setHeader("Barras P")
  }

  fun Grid<NfPrecEntrada>.notaBarcoden() = addColumnString(NfPrecEntrada::barcoden) {
    this.setHeader("Barras N")
  }

  fun Grid<NfPrecEntrada>.notaRefPrdp() = addColumnString(NfPrecEntrada::refPrdp) {
    this.setHeader("Ref P")
  }

  fun Grid<NfPrecEntrada>.notaRefPrdn() = addColumnString(NfPrecEntrada::refPrdn) {
    this.setHeader("Ref N")
  }

  fun Grid<NfPrecEntrada>.notaFrete() = addColumnDouble(NfPrecEntrada::frete) {
    this.setHeader("Frete$")
  }

  fun Grid<NfPrecEntrada>.notaFretep() = addColumnDouble(NfPrecEntrada::fretep) {
    this.setHeader("Frete P")
  }

  fun Grid<NfPrecEntrada>.notaFreten() = addColumnDouble(NfPrecEntrada::freten) {
    this.setHeader("Frete N")
  }

  fun Grid<NfPrecEntrada>.notaNcmn() = addColumnString(NfPrecEntrada::ncmn) {
    this.setHeader("NCM N")
  }


  fun Grid<NfPrecEntrada>.notaNcmp() = addColumnString(NfPrecEntrada::ncmp) {
    this.setHeader("NCM P")
  }
}