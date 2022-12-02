package br.com.astrosoft.devolucao.view.agenda.columns

import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object AgendaViewColumns {
  fun Grid<Agenda>.agendaLoja() = addColumnInt(Agenda::loja) {
    this.setHeader("Loja")
    this.isExpand = false
  }

  fun Grid<Agenda>.agendaData() = addColumnLocalDate(Agenda::data) {
    this.setHeader("Data")
    this.isExpand = false
    this.isAutoWidth = false
    this.width = "90px"
  }

  fun Grid<Agenda>.agendaColeta() = addColumnLocalDate(Agenda::coleta) {
    this.setHeader("Coleta")
    this.isExpand = false
    this.isAutoWidth = false
    this.width = "90px"
  }

  fun Grid<Agenda>.agendaCte() = addColumnString(Agenda::conhecimento) {
    this.setHeader("CTe")
    this.isExpand = false
    this.right()
  }

  fun Grid<Agenda>.agendaHora() = addColumnString(Agenda::hora) {
    this.setHeader("Hora")
    this.isExpand = false
    this.isAutoWidth = false
    this.width = "80px"
  }

  fun Grid<Agenda>.agendaRecebedor() = addColumnString(Agenda::recebedor) {
    this.setHeader("Recebedor")
  }

  fun Grid<Agenda>.agendaDataHoraRecebedor() = addColumnString(Agenda::dataHoraRecebimento) {
    this.setHeader("Data Hora Recebedor")
    this.setSortProperty(Agenda::dataRecbedor.name, Agenda::horaRecebedor.name)
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

  fun Grid<Agenda>.agendaFrete() = addColumnString(Agenda::frete) {
    this.setHeader("Frete")
    this.isExpand = false
  }

  fun Grid<Agenda>.agendaEmissao() = addColumnLocalDate(Agenda::emissao) {
    this.setHeader("Emissão")
  }

  fun Grid<Agenda>.agendaNf() = addColumnString(Agenda::nf) {
    this.setHeader("NF")
    this.isExpand = false
  }

  fun Grid<Agenda>.agendaVolume() = addColumnString(Agenda::volume) {
    this.setHeader("Volume")
    this.isExpand = false
    this.right()
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