package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid
import java.time.LocalDate

object NotaSaidaViewColumns {
  fun Grid<NotaSaida>.notaLoja() = addColumnInt(NotaSaida::loja) {
    this.setHeader("Loja")
  }

  fun Grid<NotaSaida>.notaPdv() = addColumnInt(NotaSaida::pdv) {
    this.setHeader("Pdv")
  }

  fun Grid<NotaSaida>.notaTransacao() = addColumnInt(NotaSaida::transacao) {
    this.setHeader("Transacao")
  }

  fun Grid<NotaSaida>.notaPedido() = addColumnInt(NotaSaida::pedido) {
    this.setHeader("Pedido")
  }

  fun Grid<NotaSaida>.notaDataPedido() = addColumnLocalDate(NotaSaida::dataPedido) {
    this.setHeader("Data")
  }

  fun Grid<NotaSaida>.notaNota() = addColumnString(NotaSaida::nota) {
    this.setHeader("Nota")
  }

  fun Grid<NotaSaida>.chaveDesconto() = addColumnString(NotaSaida::chaveDesconto) {
    this.setHeader("Observação")
    this.setClassNameGenerator { "marcaDiferenca" }
  }

  fun Grid<NotaSaida>.dataAgendaDesconto(situacao: ESituacaoPendencia?) = addColumnLocalDate(NotaSaida::dataAgenda) {
    this.setHeader(situacao?.dataCol ?: "Data")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }

    this.setClassNameGenerator {
      val data = it.dataAgenda ?: return@setClassNameGenerator ""
      when {
        it.situacao == "CREDITO_APLICADO" -> "marcaDiferenca"
        data.isAfter(LocalDate.now())     -> "marcaDiferenca"
        else                              -> "marcaRed"
      }
    }
  }

  fun Grid<NotaSaida>.dataSituacaoDesconto() = addColumnLocalDate(NotaSaida::dataSituacao) {
    this.setHeader("Data Sit")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }
  }

  fun Grid<NotaSaida>.situacaoDesconto() = addColumnString(NotaSaida::situacaoStr) {
    this.setHeader("Crédito")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }
  }

  fun Grid<NotaSaida>.usuarioSituacao() = addColumnString(NotaSaida::usuarioSituacao) {
    this.setHeader("Usuário")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }
  }

  fun Grid<NotaSaida>.docSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::docSituacao) {
    this.setHeader(situacao?.docCol ?: "Doc")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }
  }

  fun Grid<NotaSaida>.tituloSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::tituloSituacao) {
    this.setHeader(situacao?.numeroCol ?: "Número")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }
  }

  fun Grid<NotaSaida>.niSituacao() = addColumnString(NotaSaida::niSituacao) {
    this.setHeader("NI")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }
  }

  fun Grid<NotaSaida>.notaFatura() = addColumnString(NotaSaida::fatura) {
    this.setHeader("Fatura")
  }

  fun Grid<NotaSaida>.notaObservacao() = addColumnString(NotaSaida::remarks) {
    this.setHeader("Obs")
  }

  fun Grid<NotaSaida>.notaDataNota() = addColumnLocalDate(NotaSaida::dataNota) {
    this.setHeader("Data")
  }

  fun Grid<NotaSaida>.notaValor() = addColumnDouble(NotaSaida::valorNota) {
    this.setHeader("Valor")
  }
}
