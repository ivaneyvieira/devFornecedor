package br.com.astrosoft.framework.view.vaadin

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.viewmodel.TabUsrViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.select
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.value.ValueChangeMode
import kotlin.reflect.KMutableProperty1

abstract class TabPanelUser(val viewModel: TabUsrViewModel) : TabPanelGrid<UserSaci>(UserSaci::class) {
  lateinit var edtPesquisa: TextField

  abstract fun Grid<UserSaci>.configGrid()
  override fun Grid<UserSaci>.gridPanel() {
    this.format()

    columnGrid(UserSaci::no, "Código")
    columnGrid(UserSaci::login, "Login")
    columnGrid(UserSaci::name, "Nome")

    this.configGrid()
  }

  fun filter(): String {
    return edtPesquisa.value ?: ""
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtPesquisa = textField("Pesquisa") {
      this.width = "300px"
      this.valueChangeMode = ValueChangeMode.TIMEOUT
      this.addValueChangeListener {
        viewModel.updateView()
      }
    }
    button("Adicionar") {
      this.icon = VaadinIcon.PLUS.create()

      addClickListener {
        viewModel.adicionaUsuario()
      }
    }
    button("Atualizar") {
      this.icon = VaadinIcon.REFRESH.create()

      addClickListener {
        viewModel.modificarUsuario()
      }
    }
    button("Remove") {
      this.icon = VaadinIcon.TRASH.create()

      addClickListener {
        viewModel.removeUsuario()
      }
    }
  }

  override val label: String
    get() = "Usuários"

  override fun updateComponent() {
    viewModel.updateView()
  }

  fun updateUsuarios(usuarios: List<UserSaci>) {
    updateGrid(usuarios)
  }

  fun selectedItem(): UserSaci? {
    return itensSelecionados().firstOrNull()
  }

  abstract fun FormUsuario.configFields()

  private fun FormUsuario.configFieldsDefault(isReadOnly: Boolean) {
    textField("Login do Usuário") {
      this.isReadOnly = isReadOnly
      this.width = "300px"
      binder.bind(this, UserSaci::login.name)
    }
    textField("Nome do Usuário") {
      this.isReadOnly = isReadOnly
      this.width = "300px"
      this.isReadOnly = true
      binder.bind(this, UserSaci::name.name)
    }
  }

  fun formUpdUsuario(usuario: UserSaci) {
    println("Pedido pendente: ${usuario.pedidoPendente}")
    println("Pedido editor: ${usuario.pedidoEditor}")
    println("Pedido finalizado: ${usuario.pedidoFinalizado}")
    val form = FormUsuario(usuario) {
      this.width = "60%"
      this.configFieldsDefault(true)
      this.configFields()
    }
    DialogHelper.showForm(caption = "Usuário", form = form) {
      viewModel.updUser(form.userSaci)
    }
  }

  fun formAddUsuario() {

    val form = FormUsuario(UserSaci()) {
      this.width = "60%"
      this.configFieldsDefault(false)
      this.configFields()
    }
    DialogHelper.showForm(caption = "Usuário", form = form) {
      val usuario = form.userSaci
      println("Pedido no: ${usuario.no}")
      usuario.print()
      viewModel.addUser(form.userSaci)
    }
  }

  protected fun HasComponents.filtroLoja(
    binder: Binder<UserSaci>,
    property: KMutableProperty1<UserSaci, Int?>,
    label: String = "Nome Loja"
  ) {
    select<Int>(label) {
      this.setWidthFull()
      val lojas = viewModel.findAllLojas()
      val lojasNum = lojas.map { it.no } + listOf(0)
      setItems(lojasNum.distinct().sorted())
      this.isEmptySelectionAllowed = true
      this.setItemLabelGenerator { storeno ->
        when (storeno) {
          0    -> "Todas as lojas"
          else -> lojas.firstOrNull { loja ->
            loja.no == storeno
          }?.descricao ?: ""
        }
      }
      binder.bind(this, property.name)
    }
  }
}