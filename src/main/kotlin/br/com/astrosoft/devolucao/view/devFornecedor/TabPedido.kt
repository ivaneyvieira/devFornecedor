package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.Pedido
import br.com.astrosoft.devolucao.model.beans.ProdutosPedido
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.IPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.PedidoViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.icon.VaadinIcon.PHONE_LANDLINE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

class TabPedido(val viewModel: PedidoViewModel): TabPanelGrid<Pedido>(Pedido::class), IPedido {
  private lateinit var edtFiltro: TextField
  
  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        viewModel.updateFiltro()
      }
    }
  }
  
  override fun Grid<Pedido>.gridPanel() {
    addColumnButton(PRINT, "Produtos", "prd") {pedido ->
      showGridProdutos(pedido)
    }
    pedidoLoja()
    pedidoDataPedido()
    pedidoPedido()
    pedidoDataNota()
    pedidoNota()
    pedidoFatura()
    pedidoValor()
  }
  
  private fun showGridProdutos(pedido: Pedido?) {
    pedido ?: return
    val listaProdutos = pedido.listaProdutos()
    val form = SubWindowForm("PEDIDO: ${pedido.sigla} ${pedido.pedido}") {
      createGridProdutos(listaProdutos)
    }
    form.open()
  }
  
  private fun createGridProdutos(listaProdutos: List<ProdutosPedido>): Grid<ProdutosPedido> {
    val gridDetail = Grid(ProdutosPedido::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listaProdutos)
      //
      produtoPedCodigo()
      produtoPedDescricao()
      produtoPedGrade()
      produtoPedBarcode()
      produtoPedUn()
      produtoPedQtde()
      produtoPedValorUnitario()
      produtoPedValortotal()
    }
  }
  
  override val label: String
    get() = "Pedidos"
  
  override fun updateComponent() {
    viewModel.updateGridNota()
  }
  
  override fun filtro(): String = edtFiltro.value ?: ""
  
  override fun setFiltro(txt: String) {
    edtFiltro.value = txt
  }
}