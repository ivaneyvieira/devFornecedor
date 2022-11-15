package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.FiltroPedidoCompra
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.compra.ITabPedidosViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.TabPedidosViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.select
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabPedidos(val viewModel: TabPedidosViewModel) :
        TabPanelGrid<PedidoCompraFornecedor>(PedidoCompraFornecedor::class), ITabPedidosViewModel {
  private lateinit var edtPedquisa: TextField
  private lateinit var cmbLoja: Select<Loja>

  override fun HorizontalLayout.toolBarConfig() {
    cmbLoja = select<Loja>("Loja") {
      this.setItems(viewModel.listLojas())
      this.addValueChangeListener {
        updateComponent()
      }
    }

    edtPedquisa = textField("Pesquisa") {
      this.valueChangeMode = ValueChangeMode.TIMEOUT
      this.valueChangeTimeout = 1000
    }
  }

  override fun Grid<PedidoCompraFornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNotaPedidoCompra(viewModel).showDialogNota(fornecedor)
    }
  }

  override fun filtro(): FiltroPedidoCompra {
    return FiltroPedidoCompra(
      loja = cmbLoja.value?.no ?: 0,
      pesquisa = edtPedquisa.value ?: "",
                             )
  }

  override fun isAuthorized(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.compraPedidos
  }

  override val label: String
    get() = "Pedidos"

  override fun updateComponent() {
    viewModel.updateComponent()
  }
}