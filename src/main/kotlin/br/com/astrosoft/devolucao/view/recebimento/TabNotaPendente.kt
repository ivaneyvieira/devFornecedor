package br.com.astrosoft.devolucao.view.recebimento

import br.com.astrosoft.devolucao.model.beans.FornecedorEntrada
import br.com.astrosoft.devolucao.model.beans.NotaEntrada
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.recebimento.columns.FornecedorEntradaViewColumns.fornecedorEntradaNome
import br.com.astrosoft.devolucao.view.recebimento.columns.FornecedorEntradaViewColumns.fornecedorEntradaNumero
import br.com.astrosoft.devolucao.view.recebimento.columns.FornecedorEntradaViewColumns.notaEntradaUltimaData
import br.com.astrosoft.devolucao.view.recebimento.columns.NotaEntradaViewColumns.notaEntradaData
import br.com.astrosoft.devolucao.view.recebimento.columns.NotaEntradaViewColumns.notaEntradaHora
import br.com.astrosoft.devolucao.view.recebimento.columns.NotaEntradaViewColumns.notaEntradaLoja
import br.com.astrosoft.devolucao.view.recebimento.columns.NotaEntradaViewColumns.notaEntradaNfKey
import br.com.astrosoft.devolucao.view.recebimento.columns.NotaEntradaViewColumns.notaEntradaNota
import br.com.astrosoft.devolucao.viewmodel.recebimento.ITabNotaPendente
import br.com.astrosoft.devolucao.viewmodel.recebimento.TabNotaPendenteViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class TabNotaPendente(val viewModel: TabNotaPendenteViewModel) :
  TabPanelGrid<FornecedorEntrada>(FornecedorEntrada::class), ITabNotaPendente {
  override fun HorizontalLayout.toolBarConfig() {
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.notaPendente == true
  }

  override fun Grid<FornecedorEntrada>.gridPanel() {
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->
      showDialogNota(fornecedor)
    }

    notaEntradaUltimaData()
    fornecedorEntradaNumero()
    fornecedorEntradaNome()
  }

  private fun showDialogNota(fornecedor: FornecedorEntrada?) {
    fornecedor ?: return
    lateinit var gridNota: Grid<NotaEntrada>
    val listNotas = fornecedor.notas
    val form = SubWindowForm("DEV FORNECEDOR: ${fornecedor.vendno} ${fornecedor.fornecedor}") {
      gridNota = createGridNotas(listNotas)
      gridNota
    }
    form.open()
  }

  private fun createGridNotas(listNotas: List<NotaEntrada>): Grid<NotaEntrada> {
    val gridDetail = Grid(NotaEntrada::class.java, false)
    return gridDetail.apply {
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(MULTI)
      setItems(listNotas) //
      notaEntradaLoja()
      notaEntradaNota()
      notaEntradaData()
      notaEntradaHora()
      notaEntradaNfKey()
    }
  }

  override val label: String
    get() = "Notas Pendentes"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
