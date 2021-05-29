package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.model.beans.Pedido
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaSapViewColumns {
  fun Grid<NotaDevolucaoSap>.notaSapLoja() = addColumnInt(NotaDevolucaoSap::storeno) {
    this.setHeader("Loja")
  }

  fun Grid<NotaDevolucaoSap>.notaSapNumero() = addColumnString(NotaDevolucaoSap::numero) {
    this.setHeader("Nota")
  }

  fun Grid<NotaDevolucaoSap>.notaSapData() = addColumnLocalDate(NotaDevolucaoSap::dataLancamento) {
    this.setHeader("Data")
  }

  fun Grid<NotaDevolucaoSap>.notaSapTotal() = addColumnDouble(NotaDevolucaoSap::saldo) {
    this.setHeader("Valor")
  }
}