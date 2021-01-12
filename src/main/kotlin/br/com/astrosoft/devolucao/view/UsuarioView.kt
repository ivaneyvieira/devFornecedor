package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.UsuarioViewModel
import br.com.astrosoft.framework.view.UserLayout
import br.com.astrosoft.framework.viewmodel.IUsuarioView
import com.github.mvysny.karibudsl.v10.checkBox
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE_O
import com.vaadin.flow.component.icon.VaadinIcon.CIRCLE_THIN
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.crudui.crud.CrudOperation
import org.vaadin.crudui.crud.CrudOperation.ADD
import org.vaadin.crudui.crud.CrudOperation.DELETE
import org.vaadin.crudui.crud.CrudOperation.READ
import org.vaadin.crudui.crud.CrudOperation.UPDATE
import org.vaadin.crudui.crud.impl.GridCrud

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Usuário")
class UsuarioView: UserLayout<UserSaci, UsuarioViewModel>(), IUsuarioView {
  override val viewModel = UsuarioViewModel(this)
  
  override fun columns() =
    listOf(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name, UserSaci::impressora.name)
  
  private fun Grid<UserSaci>.addColumnBool(caption: String, value: UserSaci.() -> Boolean) {
    val column = this.addComponentColumn {bean ->
      if(bean.value()) CHECK_CIRCLE_O.create()
      else CIRCLE_THIN.create()
    }
    column.setHeader(caption)
    column.textAlign = ColumnTextAlign.CENTER
  }
  
  override fun createGrid() = GridCrud<UserSaci>(UserSaci::class.java)
  
  override fun formCrud(operation: CrudOperation?, domainObject: UserSaci?, readOnly: Boolean,
                        binder: Binder<UserSaci>): Component {
    return FormLayout().apply {
      if(operation in listOf(READ, DELETE, UPDATE)) integerField("Número") {
        isReadOnly = true
        binder.bind(this, UserSaci::no.name)
      }
      if(operation in listOf(ADD, READ, DELETE, UPDATE)) textField("Login") {
        binder.bind(this, UserSaci::login.name)
      }
      if(operation in listOf(READ, DELETE, UPDATE)) textField("Nome") {
        isReadOnly = true
        binder.bind(this, UserSaci::name.name)
      }
      if(operation in listOf(READ, DELETE, UPDATE)) textField("Impressora") {
        isReadOnly = true
        binder.bind(this, UserSaci::impressora.name)
      }
      if(operation in listOf(ADD, READ, DELETE, UPDATE)) {
        integerField("Número Loja") {
          binder.bind(this, UserSaci::storeno.name)
        }
        checkBox("Pedido") {
          binder.bind(this, UserSaci::pedido.name)
        }
        checkBox("Nota série 66") {
          binder.bind(this, UserSaci::nota66.name)
        }
        checkBox("Nota série 1") {
          binder.bind(this, UserSaci::nota01.name)
        }
        checkBox("Nota série 66 pago") {
          binder.bind(this, UserSaci::nota66Pago.name)
        }
        checkBox("Email Recebido") {
          binder.bind(this, UserSaci::emailRecebido.name)
        }
        checkBox("Nota Pendente") {
          binder.bind(this, UserSaci::notaPendente.name)
        }
        checkBox("Nota Pendente") {
          binder.bind(this, UserSaci::agendaNaoAgendada.name)
        }
        checkBox("Nota Pendente") {
          binder.bind(this, UserSaci::agendaAgendada.name)
        }
        checkBox("Nota Pendente") {
          binder.bind(this, UserSaci::agendaRecebida.name)
        }
      }
    }
  }
}

