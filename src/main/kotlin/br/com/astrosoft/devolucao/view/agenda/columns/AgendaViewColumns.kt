package br.com.astrosoft.devolucao.view.agenda.columns

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object AgendaViewColumns {
  fun Grid<Agenda>.agendaLoja() = addColumnInt(Agenda::loja) {
    this.setHeader("Loja")
  }
  
  fun Grid<Agenda>.agendaData() = addColumnLocalDate(Agenda::data) {
    this.setHeader("Data")
  }
  
  fun Grid<Agenda>.agendaHora() = addColumnString(Agenda::hora) {
    this.setHeader("Hora")
  }
  
  fun Grid<Agenda>.agendaRecebedor() = addColumnString(Agenda::recebedor) {
    this.setHeader("Recebedor")
  }
  
  fun Grid<Agenda>.agendaOrd() = addColumnString(Agenda::invno) {
    this.setHeader("Ord")
  }
  
  fun Grid<Agenda>.agendaFornecedor() = addColumnInt(Agenda::fornecedor) {
    this.setHeader("Forn")
  }
  
  fun Grid<Agenda>.agendaAbrev() = addColumnString(Agenda::abreviacao) {
    this.setHeader("Abrev")
  }
  
  fun Grid<Agenda>.agendaEmissao() = addColumnLocalDate(Agenda::emissao) {
    this.setHeader("Emissão")
  }
  
  fun Grid<Agenda>.agendaNf() = addColumnString(Agenda::nf) {
    this.setHeader("NF")
  }
  
  fun Grid<Agenda>.agendaVolume() = addColumnString(Agenda::volume) {
    this.setHeader("Valume")
  }
  
  fun Grid<Agenda>.agendaTotal() = addColumnDouble(Agenda::total) {
    this.setHeader("Total")
  }
  
  fun Grid<Agenda>.agendaTransp() = addColumnInt(Agenda::transp) {
    this.setHeader("Transp")
  }
  
  fun Grid<Agenda>.agendaNome() = addColumnString(Agenda::nome) {
    this.setHeader("Nome")
  }
  
  fun Grid<Agenda>.agendaPedido() = addColumnInt(Agenda::pedido) {
    this.setHeader("Pedido")
  }
}