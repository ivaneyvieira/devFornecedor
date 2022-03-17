package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.configCol
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.framework.view.*
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

  fun Grid<NotaSaida>.notaNfAjuste() = addColumnString(NotaSaida::nfAjuste) {
    this.setHeader("NF Ajuste")
  }

  fun Grid<NotaSaida>.chaveDesconto() = addColumnString(NotaSaida::chaveDesconto) {
    this.setHeader("Observação")
    this.setClassNameGenerator {
      "marcaRed"
    }
  }

  fun Grid<NotaSaida>.dataAgendaDesconto(situacao: ESituacaoPendencia?) = addColumnLocalDate(NotaSaida::dataAgenda) {
    this.setHeader("Data")
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }

    this.setClassNameGenerator {
      "marcaRed"
    }

    this.configCol(situacao?.dataCol)
  }

  fun Grid<NotaSaida>.dataSituacaoDesconto(situacao: ESituacaoPendencia?) = addColumnLocalDate  (NotaSaida::dataSituacao) {
    this.setHeader("Data Sit")
    this.setClassNameGenerator {
      "marcaRed"
    }
    this.configCol(situacao?.dataSitCol)
  }

  fun Grid<NotaSaida>.situacaoDesconto(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::situacaoStr) {
    this.setHeader("Situação")
    this.setClassNameGenerator {
      "marcaRed"
    }
    this.configCol(situacao?.situacaoCol)
  }

  fun Grid<NotaSaida>.usuarioSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::usuarioSituacao) {
    this.setHeader("Usuário")
    this.configCol(situacao?.userCol)
    this.setClassNameGenerator {
      "marcaRed"
    }
  }

  fun Grid<NotaSaida>.docSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::docSituacao) {
    this.setHeader("Doc")
    this.setClassNameGenerator {
      "marcaRed"
    }
    this.configCol(situacao?.docCol)
  }

  fun Grid<NotaSaida>.notaSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::notaSituacao) {
    this.setHeader("Aguardar")
    this.setClassNameGenerator {
      "marcaRed"
    }
    this.configCol(situacao?.notaCol)
  }

  fun Grid<NotaSaida>.tituloSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::tituloSituacao) {
    this.setHeader("Número")
    this.right()
    this.setClassNameGenerator {
      "marcaRed"
    }
    this.configCol(situacao?.numeroCol)
  }

  fun Grid<NotaSaida>.niSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::niSituacao) {
    this.setHeader("NI")
    this.right()
    this.setClassNameGenerator {
      "marcaRed"
    }
    this.configCol(situacao?.niCol)
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
