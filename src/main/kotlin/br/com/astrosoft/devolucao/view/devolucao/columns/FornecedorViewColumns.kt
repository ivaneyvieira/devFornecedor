package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid
import java.time.LocalDate

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

  fun Grid<Fornecedor>.chaveDesconto() = addColumnString(Fornecedor::chaveDesconto) {
    this.setHeader("Observação")
    this.isAutoWidth = false
    this.width = "30em"
    this.setClassNameGenerator {
      val nota = it.notaObs ?: return@setClassNameGenerator ""
      if (nota.tipo != "1") return@setClassNameGenerator ""
      if (nota.isObservacaoFinanceiro()) "marcaDiferenca"
      else "marcaRed"
    }
  }

  fun Grid<Fornecedor>.dataAgendaDesconto() = addColumnLocalDate(Fornecedor::dataAgenda) {
    this.setHeader("Agenda")
    this.isAutoWidth = false
    this.width = "100px"
    this.setClassNameGenerator {
      val nota = it.notaObs ?: return@setClassNameGenerator ""
      if (nota.tipo != "1") return@setClassNameGenerator ""
      if (nota.isObservacaoFinanceiro()) "marcaDiferenca"
      else "marcaRed"
    }
  }

  fun Grid<Fornecedor>.fornecedorValorTotal() = addColumnDouble(Fornecedor::valor) {
    this.setHeader("Valor Total")
  }
}