package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.ContaRazao
import br.com.astrosoft.devolucao.model.beans.ContaRazaoNota
import br.com.astrosoft.devolucao.model.beans.FiltroContaRazaoNota
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabContaRazaoDemandaViewModel(val viewModel: DemandaViewModel) {
  fun updateView() {
    val filtro = subView.filtro()
    val lista = ContaRazaoNota.findNota(filtro)
    subView.updateGrid(lista)
  }

  private fun AgendaDemanda.valida() {
    if (titulo.isBlank()) fail("O campo título não foi informado")
    if (conteudo.isBlank()) fail("O campo conteudo está vazio")
  }

  fun showNotas(contaRazao: ContaRazao) {
    subView.showNotas(contaRazao)
  }

  val subView
    get() = viewModel.view.tabContaRazaoDemanda
}

interface ITabContaRazaoDemanda : ITabView {
  fun updateGrid(itens: List<ContaRazao>)
  fun filtro(): FiltroContaRazaoNota
  fun showNotas(fornecedor: ContaRazao)
}

