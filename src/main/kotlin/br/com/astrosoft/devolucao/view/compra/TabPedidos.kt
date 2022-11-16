package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorCompra
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorCompraResumido
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorResumido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colDataPedido
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colFornecedor
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlCancelada
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPedida
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlPendente
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraFornecedorColumns.colVlRecebida
import br.com.astrosoft.devolucao.viewmodel.compra.ITabPedidosViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.TabPedidosViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabPedidos(val viewModel: TabPedidosViewModel) :
        TabPanelGrid<PedidoCompraFornecedor>(PedidoCompraFornecedor::class), ITabPedidosViewModel {
  private lateinit var edtPedquisa: TextField
  private lateinit var edtLoja: IntegerField

  override fun HorizontalLayout.toolBarConfig() {
    edtLoja = integerField("Loja") {
      this.valueChangeMode = ValueChangeMode.TIMEOUT
      this.valueChangeTimeout = 1000
      this.addValueChangeListener {
        updateComponent()
      }
    }

    edtPedquisa = textField("Pesquisa") {
      this.valueChangeMode = ValueChangeMode.TIMEOUT
      this.valueChangeTimeout = 1000
      this.addValueChangeListener {
        updateComponent()
      }
    }

    button("Relatório") {
      icon = VaadinIcon.PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorioFornecedor(fornecedores.flatMap { it.pedidos })
      }
    }
    button("Relatório Resumido") {
      icon = VaadinIcon.PRINT.create()
      onLeftClick {
        val fornecedores = itensSelecionados()
        viewModel.imprimirRelatorioResumido(fornecedores)
      }
    }
  }

  override fun Grid<PedidoCompraFornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Pedidos", "Pedidos") { fornecedor ->
      DlgNotaPedidoCompra().showDialogNota(fornecedor)
    }

    colCodigo()
    colFornecedor()
    colDataPedido()
    colVlPedida()
    colVlCancelada()
    colVlRecebida()
    colVlPendente()
  }

  override fun filtro(): FiltroPedidoCompra {
    return FiltroPedidoCompra(
      loja = edtLoja.value ?: 0,
      pesquisa = edtPedquisa.value ?: "",
                             )
  }

  override fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>) {
    val report = RelatorioFornecedorCompra.processaRelatorio(pedido)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>) {
    val report = RelatorioFornecedorCompraResumido.processaRelatorio(fornecedores)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
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