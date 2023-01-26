package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaCliente
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaFornecedor
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaNome
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabFornecedorDemanda
import br.com.astrosoft.devolucao.viewmodel.demanda.TabFornecedorDemandaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabFornecedorDemanda(private val viewModel: TabFornecedorDemandaViewModel) :
        TabPanelGrid<FornecedorProduto>(FornecedorProduto::class), ITabFornecedorDemanda {

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "300px"
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<FornecedorProduto>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnButton(iconButton = VaadinIcon.EDIT, tooltip = "Editar", header = "Editar") { fornecedor ->
      viewModel.editar(fornecedor)
    }

    addColumnButton(iconButton = VaadinIcon.FILE, tooltip = "Anexo", header = "Anexo") { fornecedor ->
      viewModel.anexo(fornecedor)
    }.apply {
      this.setClassNameGenerator { b ->
        if (b.quantAnexo > 0) "marcaOk" else null
      }
    }

    fornecedorDemandaFornecedor()
    fornecedorDemandaCliente()
    fornecedorDemandaNome()
  }

  override fun showAnexoForm(fornecedor: FornecedorProduto) {
    val demanda = fornecedor.getDemanda()
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

  override fun showUpdateForm(fornecedor: FornecedorProduto, execUpdate: (demanda: AgendaDemanda?) -> Unit) {
    val demanda = fornecedor.getDemanda()
    showAgendaForm(demanda = demanda, title = "Edita", isReadOnly = false, exec = execUpdate)
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

  override fun filtro(): String {
    return edtFiltro.value ?: ""
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoFornecedor == true
  }

  private lateinit var edtFiltro: TextField
  override val label: String
    get() = "Fornecedor"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
