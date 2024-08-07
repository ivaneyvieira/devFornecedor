package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object UltimaNotaEntradaColumns {
  fun Grid<NfPrecEntrada>.notaLoja() = addColumnInt(NfPrecEntrada::ljCol) {
    this.setHeader("Lj")
    this.isResizable = true
    this.isExpand = false
  }

  fun Grid<NfPrecEntrada>.notaNi() = addColumnInt(NfPrecEntrada::niCol) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaData() = addColumnLocalDate(NfPrecEntrada::data, formatPattern = "dd/MM/yy") {
    this.setHeader("Entrada")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaDataEmissao() = addColumnLocalDate(NfPrecEntrada::dataEmissao) {
    this.setHeader("Emissão")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaNfe() = addColumnString(NfPrecEntrada::nfe) {
    this.setHeader("Nota")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaFornCad() = addColumnString(NfPrecEntrada::fornCad) {
    this.setHeader("F Cad")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaRefPreco() = addColumnDouble(NfPrecEntrada::precoRef) {
    this.setHeader("Preço Ref")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaFornNota() = addColumnString(NfPrecEntrada::fornNota) {
    this.setHeader("F Nota")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaProd() = addColumnString(NfPrecEntrada::prod) {
    this.setHeader("Prod")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaDescricao() = addColumnString(NfPrecEntrada::descricao) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPedidoCompra() = addColumnInt(NfPrecEntrada::pedidoCompra) {
    this.setHeader("Ped Comp")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaGrade() = addColumnString(NfPrecEntrada::grade) {
    this.setHeader("Grade")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaRotulo() = addColumnString(NfPrecEntrada::rotulo) {
    this.setHeader("Rotulo")
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
    this.isExpand = false
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaCstp() = addColumnString(NfPrecEntrada::cstp) {
    this.setHeader("CST P")
    this.isResizable = true
    this.isExpand = false
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaCstx() = addColumnString(NfPrecEntrada::cstx) {
    this.setHeader("CST X")
    this.isResizable = true
    this.isExpand = false
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaMvan() = addColumnDouble(NfPrecEntrada::mvanAprox) {
    this.setHeader("MVA N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaMvax() = addColumnDouble(NfPrecEntrada::mvax) {
    this.setHeader("MVA X")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaMvap() = addColumnDouble(NfPrecEntrada::mvap) {
    this.setHeader("MVA P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaBarcodep() = addColumnString(NfPrecEntrada::barcodep) {
    this.setHeader("Cód Barras Gtin")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaBarcodec() = addColumnString(NfPrecEntrada::barcodec) {
    this.setHeader("Cód Barras Cad")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaBarcoden() = addColumnString(NfPrecEntrada::barcoden) {
    this.setHeader("Barras N")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaBarcodex() = addColumnString(NfPrecEntrada::barcodex) {
    this.setHeader("Cód Barras XML")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaRefPrdp() = addColumnString(NfPrecEntrada::refPrdn) {
    this.setHeader("Ref P")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaRefPrdn() = addColumnString(NfPrecEntrada::refPrdn) {
    this.setHeader("Ref N")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaRefPrdx() = addColumnString(NfPrecEntrada::refPrdx) {
    this.setHeader("Ref X")
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
    this.setHeader("R$ Ped")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPrecopc() = addColumnDouble(NfPrecEntrada::precopc) {
    this.setHeader("R$ Prec")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFreteKg() = addColumnDouble(NfPrecEntrada::freteKg) {
    this.setHeader("$ F Kg")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFreteUnitario() = addColumnDouble(NfPrecEntrada::freteUnit) {
    this.setHeader("R$ Frete")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFreteTotal() = addColumnDouble(NfPrecEntrada::freteTotal) {
    this.setHeader("R$ Frete")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFretePerNf() = addColumnDouble(NfPrecEntrada::fretePerNf) {
    this.setHeader("% F NF")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaFretePerPrc() = addColumnDouble(NfPrecEntrada::fretePerPrc) {
    this.setHeader("% F Prc")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPreconUnit() = addColumnDouble(NfPrecEntrada::precon) {
    this.setHeader("R$ NF")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPreconTotal() = addColumnDouble(NfPrecEntrada::preconTotal) {
    this.setHeader("R$ NF")
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

  fun Grid<NfPrecEntrada>.notaPeson() = addColumnDouble(NfPrecEntrada::pesoBrutoTotal) {
    this.setHeader("P NF")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaPesop() = addColumnDouble(NfPrecEntrada::pesoBrutoPrd) {
    this.setHeader("P Prd")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaQuant() = addColumnInt(NfPrecEntrada::quant) {
    this.setHeader("Qtd")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVenc() = addColumnLocalDate(NfPrecEntrada::dataVal, formatPattern = "MM/yy") {
    this.setHeader("Venc")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaQuantx() = addColumnDouble(NfPrecEntrada::quantx, pattern = "#,##0.####") {
    this.setHeader("Qtd X")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaValidade() = addColumnInt(NfPrecEntrada::mesesValidade) {
    this.setHeader("Val")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaInfo() = addColumnString(NfPrecEntrada::infoPrd) {
    this.setHeader("Info")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaEstoque() = addColumnInt(NfPrecEntrada::estoque) {
    this.setHeader("Estoque")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaCDesp() = addColumnString(NfPrecEntrada::cDesp) {
    this.setHeader("C Desp")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaNcmn() = addColumnString(NfPrecEntrada::ncmn) {
    this.setHeader("NCM N")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaNcmx() = addColumnString(NfPrecEntrada::ncmx) {
    this.setHeader("NCM X")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaNcmp() = addColumnString(NfPrecEntrada::ncmp) {
    this.setHeader("NCM P")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaCFOP() = addColumnString(NfPrecEntrada::cfop) {
    this.setHeader("CFOP")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaCFOPX() = addColumnString(NfPrecEntrada::cfopx) {
    this.setHeader("CFOP X")
    this.isResizable = true
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaCst() = addColumnString(NfPrecEntrada::cstIcms) {
    this.setHeader("CST N")
    this.isResizable = true
    this.isExpand = false
    this.right()
  }

  fun Grid<NfPrecEntrada>.notaValor() = addColumnDouble(NfPrecEntrada::valor) {
    this.setHeader("Valor")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlDesconto() = addColumnDouble(NfPrecEntrada::vlDesconto) {
    this.setHeader("Desc")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlLiquido() = addColumnDouble(NfPrecEntrada::vlLiquido) {
    this.setHeader("V Liq")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlFrete() = addColumnDouble(NfPrecEntrada::vlFrete) {
    this.setHeader("V Fret")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlDespesa() = addColumnDouble(NfPrecEntrada::vlDespesas) {
    this.setHeader("V Desp")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlIcms() = addColumnDouble(NfPrecEntrada::vlIcms) {
    this.setHeader("V ICMS")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlIcmsx() = addColumnDouble(NfPrecEntrada::vlIcmsx) {
    this.setHeader("V ICMS")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlPisx() = addColumnDouble(NfPrecEntrada::vlPisx) {
    this.setHeader("V PIS")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlCofinsx() = addColumnDouble(NfPrecEntrada::vlCofinsx) {
    this.setHeader("V Cofins")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaAlIcmsx() = addColumnDouble(NfPrecEntrada::alIcmsx) {
    this.setHeader("V ICMS")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaAlPisx() = addColumnDouble(NfPrecEntrada::alPisx) {
    this.setHeader("V PIS")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaAlCofinsx() = addColumnDouble(NfPrecEntrada::alCofinsx) {
    this.setHeader("V Cofins")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaDescricaox() = addColumnString(NfPrecEntrada::descricaox) {
    this.setHeader("Descrição")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaUnidadex() = addColumnString(NfPrecEntrada::unidadex) {
    this.setHeader("Unid")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaUnidade() = addColumnString(NfPrecEntrada::unidade) {
    this.setHeader("Unid")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlIpi() = addColumnDouble(NfPrecEntrada::vlIpi) {
    this.setHeader("V IPI")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlIpix() = addColumnDouble(NfPrecEntrada::vlIpix) {
    this.setHeader("V IPI")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaAlIpix() = addColumnDouble(NfPrecEntrada::alIpix) {
    this.setHeader("V IPI")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaBaseSubst() = addColumnDouble(NfPrecEntrada::baseSubst) {
    this.setHeader("Base ST")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaBaseSubstx() = addColumnDouble(NfPrecEntrada::baseSubstx) {
    this.setHeader("Base ST")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlSubst() = addColumnDouble(NfPrecEntrada::vlIcmsSubst) {
    this.setHeader("Valor ST")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlSubstx() = addColumnDouble(NfPrecEntrada::vlIcmsSubstx) {
    this.setHeader("Valor ST")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlTotal() = addColumnDouble(NfPrecEntrada::vlTotal) {
    this.setHeader("V Total")
    this.isResizable = true
  }

  fun Grid<NfPrecEntrada>.notaVlTotalx() = addColumnDouble(NfPrecEntrada::valorx) {
    this.setHeader("V Total")
    this.isResizable = true
  }
}