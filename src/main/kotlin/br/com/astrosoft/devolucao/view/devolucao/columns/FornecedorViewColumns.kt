package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.framework.view.*
import com.vaadin.flow.component.grid.Grid

object FornecedorViewColumns {
  fun Grid<Fornecedor>.fornecedorCodigo() = addColumnInt(Fornecedor::vendno) {
    this.setHeader("Fornecedor")
  }

  fun Grid<Fornecedor>.fornecedorCliente() = addColumnInt(Fornecedor::custno) {
    this.setHeader("Cliente")
  }

  fun Grid<Fornecedor>.fornecedorNome() = addColumnString(Fornecedor::fornecedor) {
    this.setHeader("Fornecedor")
  }

  fun Grid<Fornecedor>.fornecedorPrimeiraData() = addColumnLocalDate(Fornecedor::primeiraData) {
    this.setHeader("Primeira Data")
  }

  fun Grid<Fornecedor>.fornecedorUltimaData() = addColumnLocalDate(Fornecedor::ultimaData) {
    this.setHeader("Ultima Data")
  }

  fun Grid<Fornecedor>.observacaoChaveDesconto() = addColumnString(Fornecedor::chaveDesconto) {
    this.setHeader("Observação")
    this.isAutoWidth = false
    this.width = "18em"
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.dataAgendaDesconto() = addColumnLocalDate(Fornecedor::dataAgenda) {
    this.setHeader("Data")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.fornecedorValorTotal() = addColumnDouble(Fornecedor::valorTotal) {
    this.setHeader("Valor Total")
  }

  fun Grid<Fornecedor>.dataSituacaoDesconto() = addColumnLocalDate(Fornecedor::dataSituacao) {
    this.setHeader("Data Sit")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.notaEditavel() = addColumnString(Fornecedor::notaEditavel) {
    this.setHeader("NFA")
    this.right()
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
  }

  fun Grid<Fornecedor>.dataNotaEditavel() = addColumnLocalDate(Fornecedor::dataNotaEditavel) {
    this.setHeader("Data Nota")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor
    }
  }

  fun Grid<Fornecedor>.situacaoDesconto() = addColumnString(Fornecedor::situacaoStr) {
    this.setHeader("Situação")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.notaSituacao() = addColumnString(Fornecedor::notaSituacao) {
    this.setHeader("Aguardar")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.usuarioSituacao() = addColumnString(Fornecedor::usuarioSituacao) {
    this.setHeader("Usuário")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.docSituacao() = addColumnString(Fornecedor::docSituacao) {
    this.setHeader("Doc")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.fornecedorObservacao() = addColumnString(Fornecedor::remarks) {
    this.setHeader("Observação")
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.tituloSituacao() = addColumnString(Fornecedor::tituloSituacao) {
    this.setHeader("Número")
    this.right()
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }

  fun Grid<Fornecedor>.niSituacao() = addColumnString(Fornecedor::niSituacao) {
    this.setHeader("NI")
    this.right()
    this.setClassNameGenerator {
      it.situacaoPendencia?.cssCor ?: "marcaRed"
    }
  }
}