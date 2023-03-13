package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaCNPJ
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaCidade
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaCliente
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaFantasia
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaFornecedor
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaNome
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaUF
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

    addColumnButton(iconButton = VaadinIcon.MODAL_LIST, tooltip = "Nota", header = "Nota") { fornecedor ->
      viewModel.showNotas(fornecedor)
    }

    fornecedorDemandaFornecedor()
    fornecedorDemandaCliente()
    fornecedorDemandaNome()
    fornecedorDemandaFantasia()
    fornecedorDemandaCNPJ()
    fornecedorDemandaCidade()
    fornecedorDemandaUF()
  }

  override fun showAnexoForm(fornecedor: FornecedorProduto) {
    val form = FormAnexoFornecedor(fornecedor, false) {
      updateComponent()
    }
    ConfirmDialog
      .create()
      .withCaption("Anexos")
      .withMessage(form)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  override fun showUpdateForm(fornecedor: FornecedorProduto, execUpdate: (demanda: FornecedorProduto?) -> Unit) {
    showAgendaForm(fornecedor = fornecedor, title = "Edita", isReadOnly = false, exec = execUpdate)
  }

  private fun showAgendaForm(fornecedor: FornecedorProduto?,
                             title: String,
                             isReadOnly: Boolean,
                             exec: (demanda: FornecedorProduto?) -> Unit) {
    val form = FormFornecedor(fornecedor, isReadOnly)
    ConfirmDialog.create().withCaption(title).withMessage(form).withOkButton({
                                                                               val bean = form.bean
                                                                               exec(bean)
                                                                             }).withCancelButton().open()
  }

  override fun filtro(): String {
    return edtFiltro.value ?: ""
  }

  override fun showNotas(fornecedor: FornecedorProduto) {
    DlgFornecedorNota(fornecedor).showDialogNota()
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
