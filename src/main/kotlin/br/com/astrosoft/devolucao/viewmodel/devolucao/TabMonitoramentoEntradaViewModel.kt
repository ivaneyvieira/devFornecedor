package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.FiltroMonitoramentoEntrada
import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntrada
import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntradaFornecedor
import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntradaNota
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabMonitoramentoEntradaViewModel(val viewModel: DevolucaoPedidoViewModel) {
  fun updateView() {
    val filtro = subView.filtro()
    val lista = MonitoramentoEntrada.findMonitoramento(filtro)
    subView.updateGrid(lista)
  }

  fun imprimirNota(notas: List<MonitoramentoEntradaNota>) {
        notas.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimeSelecionados(notas)
  }

  val subView
    get() = viewModel.view.tabMonitoramentoEntrada
}

interface ITabMonitoramentoEntrada : ITabView {
  fun updateGrid(itens: List<MonitoramentoEntradaFornecedor>)
  fun filtro(): FiltroMonitoramentoEntrada
  fun imprimeSelecionados(notas: List<MonitoramentoEntradaNota>)
}

