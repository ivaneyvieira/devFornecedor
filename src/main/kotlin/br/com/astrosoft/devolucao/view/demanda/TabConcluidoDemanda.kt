package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.FilterAgendaDemanda
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaConteudo
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaData
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaDestino
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaOrigem
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaTitulo
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabConcluidoDemanda
import br.com.astrosoft.devolucao.viewmodel.demanda.TabConcluidoDemandaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabConcluidoDemanda(val viewModel: TabConcluidoDemandaViewModel) :
        TabPanelGrid<AgendaDemanda>(AgendaDemanda::class), ITabConcluidoDemanda {
  private lateinit var edtFiltro: TextField
  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    button("Voltar") {
      this.icon = VaadinIcon.ARROW_LEFT.create()

      onLeftClick {
        viewModel.voltaDemanda()
      }
    }
  }

  override fun Grid<AgendaDemanda>.gridPanel() {
    setSelectionMode(SelectionMode.MULTI)

    addColumnButton(iconButton = VaadinIcon.SEARCH, tooltip = "Visualizar", header = "Visualizar") { demanda ->
      viewModel.visualizar(demanda)
    }

    addColumnButton(iconButton = VaadinIcon.FILE, tooltip = "Anexo", header = "Anexo") { demanda ->
      viewModel.anexo(demanda)
    }.apply {
      this.setClassNameGenerator {b ->
        if(b.quantAnexo > 0) "marcaOk" else null
      }
    }

    colDemandaData()
    colDemandaOrigem()
    colDemandaDestino()
    colDemandaTitulo()
    colDemandaConteudo()
  }

  override fun showAnexoForm(demanda: AgendaDemanda) {
    val form = FormAnexo(demanda, false) {
      updateComponent()
    }
    ConfirmDialog
      .create()
      .withCaption("Anexos")
      .withMessage(form)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  override fun showForm(demanda: AgendaDemanda) {
    val form = FormAgendaDemanda(demanda, true)
    ConfirmDialog
      .create()
      .withCaption("Agenda")
      .withMessage(form)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  override fun filter(): FilterAgendaDemanda {
    return FilterAgendaDemanda(pesquisa = edtFiltro.value ?: "", concluido = true, vendno = 0)
  }

  override fun selectedItem(): List<AgendaDemanda> {
    return itensSelecionados()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.demandaConcluido == true
  }

  override val label: String
    get() = "Concluído"

  override fun updateComponent() {
    viewModel.updateView()
  }
}

