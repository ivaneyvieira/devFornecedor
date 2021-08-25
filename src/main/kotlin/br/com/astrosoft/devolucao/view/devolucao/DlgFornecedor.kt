package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaCelular
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaEmail
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaRepresentante
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaTelefone
import br.com.astrosoft.framework.view.SubWindowForm
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant

class DlgFornecedor {
  fun showDialogRepresentante(fornecedor: Fornecedor?) {
    fornecedor ?: return
    val listRepresentantes = fornecedor.listRepresentantes()
    val form = SubWindowForm(fornecedor.labelTitle) {
      createGridRepresentantes(listRepresentantes)
    }
    form.open()
  }

  private fun createGridRepresentantes(listRepresentantes: List<Representante>): Grid<Representante> {
    val gridDetail = Grid(Representante::class.java, false)
    return gridDetail.apply {
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setItems(listRepresentantes) //
      notaRepresentante()
      notaTelefone()
      notaCelular()
      notaEmail()
    }
  }
}