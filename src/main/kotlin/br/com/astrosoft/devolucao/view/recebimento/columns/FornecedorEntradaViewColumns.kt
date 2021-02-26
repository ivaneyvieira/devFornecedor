package br.com.astrosoft.devolucao.view.recebimento.columns

import br.com.astrosoft.devolucao.model.beans.FornecedorEntrada
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object FornecedorEntradaViewColumns {
    fun Grid<FornecedorEntrada>.fornecedorEntradaNumero() =
        addColumnInt(FornecedorEntrada::vendno) {
            this.setHeader("Número")
        }

    fun Grid<FornecedorEntrada>.notaEntradaUltimaData() =
        addColumnLocalDate(FornecedorEntrada::ultimaData) {
            this.setHeader("Ultima Data")
        }

    fun Grid<FornecedorEntrada>.fornecedorEntradaNome() =
        addColumnString(FornecedorEntrada::fornecedor) {
            this.setHeader("Fornecedor")
        }
}