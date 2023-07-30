package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabFornecedorDemandaViewModel(val viewModel: DemandaViewModel) {
  fun updateView() {
    val filtro = subView.filtro()
    val lista = FornecedorProduto.findAll(filtro).toList()
    subView.updateGrid(lista)
  }

  fun editar(fornecedor: FornecedorProduto) {
    subView.showUpdateForm(fornecedor) { forn ->
      forn?.save()
      updateView()
    }
  }

  private fun AgendaDemanda.valida() {
    if (titulo.isBlank()) fail("O campo título não foi informado")
    if (conteudo.isBlank()) fail("O campo conteudo está vazio")
  }

  fun anexo(fornecedor: FornecedorProduto) = viewModel.exec {
    subView.showAnexoForm(fornecedor)
  }

  fun showNotas(fornecedor: FornecedorProduto) {
    subView.showNotas(fornecedor)
  }

  val subView
    get() = viewModel.view.tabFornecedorDemanda
}

interface ITabFornecedorDemanda : ITabView {
  fun showAnexoForm(fornecedor: FornecedorProduto)
  fun showUpdateForm(fornecedor: FornecedorProduto, execUpdate: (fornecedor: FornecedorProduto?) -> Unit)
  fun updateGrid(itens: List<FornecedorProduto>)
  fun filtro(): String
  fun showNotas(fornecedor: FornecedorProduto)
}

