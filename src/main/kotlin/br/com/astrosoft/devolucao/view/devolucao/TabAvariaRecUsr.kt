package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAvariaRecUsr
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAvariaRecUsrViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.vaadin.*
import com.github.mvysny.karibudsl.v10.checkBox
import com.vaadin.flow.component.grid.Grid

class TabAvariaRecUsr(viewModel: TabAvariaRecUsrViewModel) : TabPanelUser(viewModel), ITabAvariaRecUsr {
  override fun Grid<UserSaci>.configGrid() {
    columnGrid(UserSaci::avariaRecEditor, "Editor")
    columnGrid(UserSaci::avariaRecPendente, "Pendente")
    columnGrid(UserSaci::avariaRecTransportadora, "Transportadora")
    columnGrid(UserSaci::avariaRecEmail, "E-mail")
  }

  override fun FormUsuario.configFields() {
    horizontalBlock {
      verticalBlock("Menus") {
        checkBox("Editor") {
          binder.bind(this, UserSaci::avariaRecEditor.name)
        }
        checkBox("Pendente") {
          binder.bind(this, UserSaci::avariaRecPendente.name)
        }
        checkBox("Transportadora") {
          binder.bind(this, UserSaci::avariaRecTransportadora.name)
        }
        checkBox("E-mail") {
          binder.bind(this, UserSaci::avariaRecEmail.name)
        }
      }
      verticalBlock("Comandos")
    }
  }

  override fun isAuthorized(user: IUser): Boolean {
    return user.admin
  }
}