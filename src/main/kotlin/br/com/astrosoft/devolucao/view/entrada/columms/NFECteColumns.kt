package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.NfEntradaFrete
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NFECteColumns {
  fun Grid<NfEntradaFrete>.notaLoja() = addColumnInt(NfEntradaFrete::loja) {
    this.setHeader("Loja")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaNI() = addColumnString(NfEntradaFrete::ni) {
    this.setHeader("NI")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaNF() = addColumnString(NfEntradaFrete::nf) {
    this.setHeader("NF")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaEmissao() = addColumnLocalDate(NfEntradaFrete::emissao, formatPattern = "dd/MM/yy") {
    this.setHeader("Emissão")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaEntrada() = addColumnLocalDate(NfEntradaFrete::entrada, formatPattern = "dd/MM/yy") {
    this.setHeader("Entrada")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaForNf() = addColumnInt(NfEntradaFrete::vendno) {
    this.setHeader("For NF")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaTotalPrd() = addColumnDouble(NfEntradaFrete::totalPrd) {
    this.setHeader("R$ Prd")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaTotalNf() = addColumnDouble(NfEntradaFrete::valorNF) {
    this.setHeader("R$ NF")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaTransp() = addColumnInt(NfEntradaFrete::carrno) {
    this.setHeader("Transp")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaTranspName() = addColumnString(NfEntradaFrete::carrName) {
    this.setHeader("Nome")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaCte() = addColumnInt(NfEntradaFrete::cte) {
    this.setHeader("CTe")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaEmissaoCte() =
    addColumnLocalDate(NfEntradaFrete::emissaoCte, formatPattern = "dd/MM/yy") {
      this.setHeader("Emissão")
      this.isResizable = true
    }

  fun Grid<NfEntradaFrete>.notaEntradaCte() =
    addColumnLocalDate(NfEntradaFrete::entradaCte, formatPattern = "dd/MM/yy") {
      this.setHeader("Entrada")
      this.isResizable = true
    }

  fun Grid<NfEntradaFrete>.notaValorFrete() = addColumnDouble(NfEntradaFrete::valorCte) {
    this.setHeader("R$ Frete Fat")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaPBruto() = addColumnInt(NfEntradaFrete::pesoBruto) {
    this.setHeader("P Bruto")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaPesoCub() = addColumnDouble(NfEntradaFrete::pesoCub) {
    this.setHeader("Peso Cub")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaCub() = addColumnDouble(NfEntradaFrete::cub) {
    this.setHeader("Cub")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaFPeso() = addColumnDouble(NfEntradaFrete::fPeso) {
    this.setHeader("R$ F Peso")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaAdValore() = addColumnDouble(NfEntradaFrete::adValore) {
    this.setHeader("R$ Adv")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaGris() = addColumnDouble(NfEntradaFrete::gris) {
    this.setHeader("R$ Gris")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaTaxa() = addColumnDouble(NfEntradaFrete::taxa) {
    this.setHeader("Taxa")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaOutros() = addColumnDouble(NfEntradaFrete::outro) {
    this.setHeader("Outros")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaAliquota() = addColumnDouble(NfEntradaFrete::aliquota) {
    this.setHeader("Aliqt")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaICMS() = addColumnDouble(NfEntradaFrete::icms) {
    this.setHeader("ICMS")
    this.isResizable = true
  }

  fun Grid<NfEntradaFrete>.notaTotalFrete() = addColumnDouble(NfEntradaFrete::totalFrete) {
    this.setHeader("R$ Frete Cal")
    this.isResizable = true
  }
}
