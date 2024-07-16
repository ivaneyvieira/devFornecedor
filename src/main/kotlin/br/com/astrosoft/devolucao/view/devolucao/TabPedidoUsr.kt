package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedidoUsr
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoUsrViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.vaadin.*
import com.github.mvysny.karibudsl.v10.checkBox
import com.vaadin.flow.component.grid.Grid

class TabPedidoUsr(viewModel: TabPedidoUsrViewModel) : TabPanelUser(viewModel), ITabPedidoUsr {
  override fun Grid<UserSaci>.configGrid() {
    columnGrid(UserSaci::pedidoEditor, "Editor")
    columnGrid(UserSaci::pedidoMonitoramentoEntrada, "Produto Entrada")
    columnGrid(UserSaci::pedidoPendente, "Pendente")
    columnGrid(UserSaci::pedidoFinalizado, "Finalizado")
  }

  override fun FormUsuario.configFields() {
    horizontalBlock {
      verticalBlock("Menus") {
        checkBox("Editor") {
          binder.bind(this, UserSaci::pedidoEditor.name)
        }
        checkBox("Produto Entrada") {
          binder.bind(this, UserSaci::pedidoMonitoramentoEntrada.name)
        }
        checkBox("Pendente") {
          binder.bind(this, UserSaci::pedidoPendente.name)
        }
        checkBox("Finalizado") {
          binder.bind(this, UserSaci::pedidoFinalizado.name)
        }
      }
      verticalBlock("Comandos")
    }
  }

  override fun isAuthorized(user: IUser): Boolean {
    return user.admin
  }
}