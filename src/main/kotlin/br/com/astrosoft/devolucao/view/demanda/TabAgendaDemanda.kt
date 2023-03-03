package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.FilterAgendaDemanda
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaConteudo
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaData
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaDestino
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaOrigem
import br.com.astrosoft.devolucao.view.demanda.columns.DemandaColumns.colDemandaTitulo
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabAgendaDemanda
import br.com.astrosoft.devolucao.viewmodel.demanda.TabAgendaDemandaViewModel
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
import java.time.LocalDate

class TabAgendaDemanda(val viewModel: TabAgendaDemandaViewModel) : TabPanelGrid<AgendaDemanda>(AgendaDemanda::class),
        ITabAgendaDemanda {
  private lateinit var edtFiltro: TextField
  override fun HorizontalLayout.toolBarConfig() {
    button("Adicionar") {
      icon = VaadinIcon.PLUS.create()
      onLeftClick {
        viewModel.adicionar()
      }
    }

    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    button("Concluído") {
      this.icon = VaadinIcon.ARROW_RIGHT.create()

      onLeftClick {
        viewModel.concluiDemanda()
      }
    }
  }

  override fun Grid<AgendaDemanda>.gridPanel() {
    setSelectionMode(SelectionMode.MULTI)

    addColumnButton(iconButton = VaadinIcon.EDIT, tooltip = "Editar", header = "Editar") { demanda ->
      viewModel.editar(demanda)
    }

    addColumnButton(iconButton = VaadinIcon.TRASH, tooltip = "Remover", header = "Remover") { demanda ->
      viewModel.remover(demanda)
    }

    addColumnButton(iconButton = VaadinIcon.FILE, tooltip = "Anexo", header = "Anexo") { demanda ->
      viewModel.anexo(demanda)
    }.apply {
      this.setClassNameGenerator { b ->
        if (b.quantAnexo > 0) "marcaOk" else null
      }
    }

    colDemandaData()
    colDemandaOrigem()
    colDemandaDestino()
    colDemandaTitulo()
    colDemandaConteudo()
  }

  private fun showAgendaForm(demanda: AgendaDemanda?,
                             title: String,
                             isReadOnly: Boolean,
                             exec: (demanda: AgendaDemanda?) -> Unit) {
    val form = FormAgendaDemanda(demanda, isReadOnly)
    ConfirmDialog.create().withCaption(title).withMessage(form).withOkButton({
                                                                               val bean = form.bean
                                                                               exec(bean)
                                                                             }).withCancelButton().open()
  }

  override fun showAnexoForm(demanda: AgendaDemanda) {
    val form = FormAnexoDemanda(demanda, false) {
      updateComponent()
    }
    ConfirmDialog
      .create()
      .withCaption("Anexos")
      .withMessage(form)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  override fun showInsertForm(execInsert: (demanda: AgendaDemanda?) -> Unit) {
    val bean = AgendaDemanda(id = 0, date = LocalDate.now(), titulo = "", conteudo = "", destino = "", origem = "")
    showAgendaForm(demanda = bean, title = "Adiciona", isReadOnly = false, exec = execInsert)
  }

  override fun showUpdateForm(demanda: AgendaDemanda, execUpdate: (demanda: AgendaDemanda?) -> Unit) {
    showAgendaForm(demanda = demanda, title = "Edita", isReadOnly = false, exec = execUpdate)
  }

  override fun showDeleteForm(demanda: AgendaDemanda, execDelete: (demanda: AgendaDemanda?) -> Unit) {
    showAgendaForm(demanda = demanda, title = "Remove", isReadOnly = true, exec = execDelete)
  }

  override fun filter(): FilterAgendaDemanda {
    return FilterAgendaDemanda(pesquisa = edtFiltro.value ?: "", concluido = false, vendno = 0)
  }

  override fun selectedItem(): List<AgendaDemanda> {
    return itensSelecionados()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.demandaAgenda == true
  }

  override val label: String
    get() = "Demanda"

  override fun updateComponent() {
    viewModel.updateView()
  }
}

