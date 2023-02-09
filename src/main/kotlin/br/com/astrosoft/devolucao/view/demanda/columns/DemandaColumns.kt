package br.com.astrosoft.devolucao.view.demanda.columns

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object DemandaColumns {
  fun Grid<AgendaDemanda>.colDemandaData() = addColumnLocalDate(AgendaDemanda::date) {
    this.setHeader("Data")
    this.isExpand = false
  }
  fun Grid<AgendaDemanda>.colDemandaTitulo() = addColumnString(AgendaDemanda::titulo) {
    this.setHeader("Título")
    this.isExpand = false
  }
  fun Grid<AgendaDemanda>.colDemandaDestino() = addColumnString(AgendaDemanda::destino) {
    this.setHeader("Destino")
    this.isExpand = false
  }
  fun Grid<AgendaDemanda>.colDemandaConteudo() = addColumnString(AgendaDemanda::conteudoSingle) {
    this.setHeader("Conteúdo")
    this.isExpand = true
  }
}