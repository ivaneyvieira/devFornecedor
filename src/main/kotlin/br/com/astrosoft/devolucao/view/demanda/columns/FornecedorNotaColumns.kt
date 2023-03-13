package br.com.astrosoft.devolucao.view.demanda.columns

import br.com.astrosoft.devolucao.model.beans.FornecedorNota
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object FornecedorNotaColumns {
  fun Grid<FornecedorNota>.fornecedorNotaLoja() = addColumnInt(FornecedorNota::loja) {
    this.setHeader("Loja")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorNota>.fornecedorNotaNI() = addColumnInt(FornecedorNota::ni) {
    this.setHeader("NI")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorNota>.fornecedorNotaNF() = addColumnString(FornecedorNota::nf) {
    this.setHeader("NF")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorNota>.fornecedorNotaEmissao() = addColumnLocalDate(FornecedorNota::emissao) {
    this.setHeader("Emissão")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorNota>.fornecedorNotaEntrada() = addColumnLocalDate(FornecedorNota::entrada) {
    this.setHeader("Entrada")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorNota>.fornecedorNotaValor() = addColumnDouble(FornecedorNota::valorNota) {
    this.setHeader("Valor Nota")
    isExpand = false
    isAutoWidth = true
  }

  fun Grid<FornecedorNota>.fornecedorNotaObs() = addColumnString(FornecedorNota::obs) {
    this.setHeader("Obs")
    isExpand = true
    isAutoWidth = true
  }
}