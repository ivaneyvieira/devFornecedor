package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabDemandaUsr
import br.com.astrosoft.devolucao.viewmodel.demanda.TabDemandaUsrViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.vaadin.*
import com.github.mvysny.karibudsl.v10.checkBox
import com.vaadin.flow.component.grid.Grid

class TabDemandaUsr(viewModel: TabDemandaUsrViewModel) : TabPanelUser(viewModel), ITabDemandaUsr {
  override fun Grid<UserSaci>.configGrid() {
    columnGrid(UserSaci::demandaAgenda, "Demanda")
    columnGrid(UserSaci::demandaConcluido, "Concluido")
    columnGrid(UserSaci::pedidoFornecedor, "Fornecedor")
  }

  override fun FormUsuario.configFields() {
    horizontalBlock {
      verticalBlock("Menus") {
        checkBox("Demanda") {
          binder.bind(this, UserSaci::demandaAgenda.name)
        }
        checkBox("Concluido") {
          binder.bind(this, UserSaci::demandaConcluido.name)
        }
        checkBox("Forncedor") {
          binder.bind(this, UserSaci::pedidoFornecedor.name)
        }
      }
      verticalBlock("Comandos")
    }
  }

  override fun isAuthorized(user: IUser): Boolean {
    return user.admin
  }
}