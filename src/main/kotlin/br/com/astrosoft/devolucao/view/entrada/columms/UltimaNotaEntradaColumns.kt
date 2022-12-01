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
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaNi() = addColumnInt(NfPrecEntrada::ni) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaData() = addColumnLocalDate(NfPrecEntrada::data) {
    this.setHeader("Data")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaDataEmissao() = addColumnLocalDate(NfPrecEntrada::dataEmissao) {
    this.setHeader("Emissão")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaNfe() = addColumnString(NfPrecEntrada::nfe) {
    this.setHeader("Nfe")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFornCad() = addColumnString(NfPrecEntrada::fornCad) {
    this.setHeader("F Cad")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFornNota() = addColumnString(NfPrecEntrada::fornNota) {
    this.setHeader("F Nota")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaProd() = addColumnString(NfPrecEntrada::prod) {
    this.setHeader("Prod")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaDescricao() = addColumnString(NfPrecEntrada::descricao) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPedidoCompra() = addColumnString(NfPrecEntrada::pedidoCompra) {
    this.setHeader("Ped Comp")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaGrade() = addColumnString(NfPrecEntrada::grade) {
    this.setHeader("Grade")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaIcmsn() = addColumnDouble(NfPrecEntrada::icmsn) {
    this.setHeader("ICMS N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaIcmsp() = addColumnDouble(NfPrecEntrada::icmsp) {
    this.setHeader("ICMS P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaIcmsr() = addColumnDouble(NfPrecEntrada::icmsc) {
    this.setHeader("ICMS R")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaRedIcms() = addColumnDouble(NfPrecEntrada::icmsd) {
    this.setHeader("Red %")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaIpin() = addColumnDouble(NfPrecEntrada::ipin) {
    this.setHeader("IPI N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaIpip() = addColumnDouble(NfPrecEntrada::ipip) {
    this.setHeader("IPI P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaCstn() = addColumnString(NfPrecEntrada::cstn) {
    this.setHeader("CST N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaCstp() = addColumnString(NfPrecEntrada::cstp) {
    this.setHeader("CST P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaMvan() = addColumnDouble(NfPrecEntrada::mvanApŕox) {
    this.setHeader("MVA N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaMvap() = addColumnDouble(NfPrecEntrada::mvap) {
    this.setHeader("MVA P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaBarcodep() = addColumnString(NfPrecEntrada::barcodep) {
    this.setHeader("Barras P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaBarcoden() = addColumnString(NfPrecEntrada::barcoden) {
    this.setHeader("Barras N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaRefPrdp() = addColumnString(NfPrecEntrada::refPrdp) {
    this.setHeader("Ref P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaRefPrdn() = addColumnString(NfPrecEntrada::refPrdn) {
    this.setHeader("Ref N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFrete() = addColumnDouble(NfPrecEntrada::frete) {
    this.setHeader("Frete$")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFretep() = addColumnDouble(NfPrecEntrada::fretep) {
    this.setHeader("Frete P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFreten() = addColumnDouble(NfPrecEntrada::freten) {
    this.setHeader("Frete N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPrecop() = addColumnDouble(NfPrecEntrada::precop) {
    this.setHeader("Preço P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPrecon() = addColumnDouble(NfPrecEntrada::precon) {
    this.setHeader("Preço N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPrecoDif() = addColumnDouble(NfPrecEntrada::precoDifValue) {
    this.setHeader("Dif")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPrecoPercen() = addColumnDouble(NfPrecEntrada::precoPercen) {
    this.setHeader("%")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPesoBruto() = addColumnDouble(NfPrecEntrada::pesoBruto) {
    this.setHeader("Peso Bruto")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaNcmn() = addColumnString(NfPrecEntrada::ncmn) {
    this.setHeader("NCM N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaNcmp() = addColumnString(NfPrecEntrada::ncmp) {
    this.setHeader("NCM P")
    this.isResizable = true
  }
}