package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.Pedido

class PedidoViewModel(val viewModel: DevFornecedorViewModel) {
  fun updateFiltro() {
    TODO("Not yet implemented")
  }
  
  fun updateGridNota() {
    subView.updateGrid(listPedidos())
  }
  
  private fun listPedidos(): List<Pedido> {
    val filtro: String = subView.filtro()
    subView.setFiltro("")
    return Pedido.findPedidos()
      .filtro(filtro)
  }
  
  private val subView
    get() = viewModel.view.tabPedido
}

private fun List<Pedido>.filtro(txt: String): List<Pedido> {
  return this.filter {
    val filtroNum = txt.toIntOrNull() ?: 0
    it.custno == filtroNum || it.vendno == filtroNum || it.fornecedor.startsWith(txt, ignoreCase = true)
  }
}

interface IPedido {
  fun updateGrid(itens: List<Pedido>)
  fun filtro(): String
  fun setFiltro(txt: String)
}