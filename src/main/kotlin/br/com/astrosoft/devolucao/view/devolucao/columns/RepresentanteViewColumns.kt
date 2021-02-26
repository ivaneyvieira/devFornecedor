package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object RepresentanteViewColumns {
    fun Grid<Representante>.notaRepresentante() = addColumnString(Representante::nome) {
        this.setHeader("Representante")
    }

    fun Grid<Representante>.notaTelefone() = addColumnString(Representante::telefone) {
        this.setHeader("Telefone")
    }

    fun Grid<Representante>.notaCelular() = addColumnString(Representante::celular) {
        this.setHeader("Celular")
    }

    fun Grid<Representante>.notaEmail() = addColumnString(Representante::email) {
        this.setHeader("E-mail")
    }
}