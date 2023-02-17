package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.configCol
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid

object NotaSaidaViewColumns {
  fun Grid<NotaSaida>.notaLoja() = addColumnInt(NotaSaida::loja) {
    this.setHeader("Loja")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaPdv() = addColumnInt(NotaSaida::pdv) {
    this.setHeader("Pdv")
  }

  fun Grid<NotaSaida>.notaTransacao() = addColumnInt(NotaSaida::transacao) {
    this.setHeader("Transacao")
  }

  fun Grid<NotaSaida>.notaPedido() = addColumnInt(NotaSaida::pedido) {
    this.setHeader("Pedido")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaPedidoEditavel() = addColumnString(NotaSaida::pedidos) {
    this.setHeader("Pedido")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaDataPedido() = addColumnLocalDate(NotaSaida::dataPedido) {
    this.setHeader("Data")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaNota() = addColumnString(NotaSaida::nota) {
    this.setHeader("Nota")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaNfAjuste() = addColumnString(NotaSaida::nfAjuste) {
    this.setHeader("NF Ajuste")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.chaveDesconto(titulo: String = "Observação") = addColumnString(NotaSaida::chaveDescontoWrap) {
    this.setHeader(titulo)
    isAutoWidth = false
    isExpand = true
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
  }

  fun Grid<NotaSaida>.dataAgendaDesconto(situacao: ESituacaoPendencia?) = addColumnLocalDate(NotaSaida::dataAgenda) {
    this.setHeader("Data")
    isAutoWidth = true
    isExpand = false
    this.setClassNameGenerator {
      if (it.situacao == "CREDITO_APLICADO") "marcaDiferenca" else ""
    }

    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }

    this.configCol(situacao?.dataCol)
  }

  fun Grid<NotaSaida>.dataSituacaoDesconto(situacao: ESituacaoPendencia?) =
    addColumnLocalDate(NotaSaida::dataSituacao) {
      this.setHeader("Data Sit")
      isAutoWidth = true
      isExpand = false

      this.setClassNameGenerator {
        it.situacaoPendencia?.cssCor
      }
      this.configCol(situacao?.dataSitCol)
    }

  fun Grid<NotaSaida>.situacaoDesconto(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::situacaoStr) {
    this.setHeader("Situação")
    isAutoWidth = true
    isExpand = false
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
    this.configCol(situacao?.situacaoCol)
  }

  fun Grid<NotaSaida>.dataNotaEditavel(situacao: ESituacaoPendencia?) =
    addColumnLocalDate(NotaSaida::dataNotaEditavel) {
      this.setHeader("Data Nota")
      isAutoWidth = true
      isExpand = false

      this.setClassNameGenerator {
        it.situacaoPendencia?.cssCor
      }
      this.configCol(situacao?.situacaoCol)
    }

  fun Grid<NotaSaida>.usuarioSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::usuarioSituacao) {
    this.setHeader("Usuário")
    isAutoWidth = true
    isExpand = false
    this.configCol(situacao?.userCol)
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
  }

  fun Grid<NotaSaida>.docSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::docSituacao) {
    this.setHeader("Doc")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
    this.configCol(situacao?.docCol)
  }

  fun Grid<NotaSaida>.notaSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::notaSituacao) {
    this.setHeader("Aguardar")
    isAutoWidth = true
    isExpand = false
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
    this.configCol(situacao?.notaCol)
  }

  fun Grid<NotaSaida>.tituloSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::tituloSituacao) {
    this.setHeader("Número")
    this.right()
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
    this.configCol(situacao?.numeroCol)
  }

  fun Grid<NotaSaida>.niSituacao(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::niSituacao) {
    this.setHeader("NI")
    this.right()
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
    this.configCol(situacao?.niCol)
  }

  fun Grid<NotaSaida>.notaEditavel(situacao: ESituacaoPendencia?) = addColumnString(NotaSaida::notaEditavel) {
    this.setHeader("NF Baixa")
    isAutoWidth = true
    isExpand = false
    this.right()
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
    this.configCol(situacao?.niCol)
  }

  fun Grid<NotaSaida>.notaFatura() = addColumnString(NotaSaida::fatura) {
    this.setHeader("Fatura")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaObservacao() = addColumnString(NotaSaida::remarks) {
    this.setHeader("Obs")
  }

  fun Grid<NotaSaida>.notaNiBonificacao() = addColumnString(NotaSaida::niBonificacao) {
    this.setHeader("NI")
  }

  fun Grid<NotaSaida>.notaNiValor() = addColumnString(NotaSaida::niValor) {
    this.setHeader("NI Valor")
  }

  fun Grid<NotaSaida>.notaValorPago() = addColumnDouble(NotaSaida::valorSituacao) {
    this.setHeader("V. Pago")
  }

  fun Grid<NotaSaida>.notaDataNota() = addColumnLocalDate(NotaSaida::dataNota) {
    this.setHeader("Data")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaValor() = addColumnDouble(NotaSaida::valorNota) {
    this.setHeader("Valor")
    isAutoWidth = true
    isExpand = false
  }

  fun Grid<NotaSaida>.notaBonificacao() = addColumnString(NotaSaida::notaBonificacao) {
    this.setHeader("NFB")
  }

  fun Grid<NotaSaida>.dataBonificacao() = addColumnLocalDate(NotaSaida::dataBonificacao) {
    this.setHeader("Data")
    isAutoWidth = true
    isExpand = false
  }
}

//TODO situações: Paga, perca e 66