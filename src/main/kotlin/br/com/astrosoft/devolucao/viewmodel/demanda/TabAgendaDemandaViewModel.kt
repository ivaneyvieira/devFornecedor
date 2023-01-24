package br.com.astrosoft.devolucao.viewmodel.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.FilterAgendaDemanda
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabAgendaDemandaViewModel(val viewModel: DemandaViewModel) {
  fun updateView() {
    val filter = subView.filter()
    val list = AgendaDemanda.findAll(filter)
    subView.updateGrid(list)
  }

  fun adicionar() = viewModel.exec {
    subView.showInsertForm {demanda ->
      demanda?.valida()
      demanda?.insert()
      updateView()
    }
  }

  fun anexo(demanda : AgendaDemanda) = viewModel.exec {
    subView.showAnexoForm(demanda)
  }

  private fun AgendaDemanda.valida() {
    if(titulo.isBlank()) fail("O campo título não foi informado")
    if(conteudo.isBlank()) fail("O campo conteudo está vazio")
  }

  fun editar(demanda: AgendaDemanda) {
    subView.showUpdateForm(demanda) {dem ->
      dem?.valida()
      dem?.save()
      updateView()
    }
  }

  fun remover(demanda: AgendaDemanda) {
    subView.showDeleteForm(demanda) {dem ->
      dem?.delete()
      updateView()
    }
  }

  fun concluiDemanda() {
    val list = subView.selectedItem()
    list.forEach {
      it.marcaConcluido()
    }
    updateView()
  }

  val subView
    get() = viewModel.view.tabAgendaDemanda
}

interface ITabAgendaDemanda : ITabView {
  fun updateGrid(itens: List<AgendaDemanda>)

  fun showAnexoForm(demanda : AgendaDemanda)
  fun showInsertForm(execInsert: (demanda: AgendaDemanda?) -> Unit)
  fun showUpdateForm(demanda: AgendaDemanda, execUpdate: (demanda: AgendaDemanda?) -> Unit)
  fun showDeleteForm(demanda: AgendaDemanda, execDelete: (demanda: AgendaDemanda?) -> Unit)
  fun filter(): FilterAgendaDemanda
  fun selectedItem(): List<AgendaDemanda>
}