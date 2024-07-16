package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.viewmodel.demanda.DemandaViewModel
import br.com.astrosoft.framework.viewmodel.ITabView

class TabMonitoramentoEntradaViewModel(val viewModel: DevolucaoPedidoViewModel) {
  fun updateView() {
    val filtro = subView.filtro()
    val lista = MonitoramentoEntrada.findMonitoramento(filtro)
    subView.updateGrid(lista)
  }

  val subView
    get() = viewModel.view.tabMonitoramentoEntrada
}

interface ITabMonitoramentoEntrada : ITabView {
  fun updateGrid(itens: List<MonitoramentoEntrada>)
  fun filtro(): FiltroMonitoramentoEntrada
}

