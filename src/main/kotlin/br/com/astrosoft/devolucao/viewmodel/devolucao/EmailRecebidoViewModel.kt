package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailDB

class EmailRecebidoViewModel(val viewModel: DevFornecedorViewModel) {
  private val subView
  get() = viewModel.view.tabEmailRecebido
  
  fun updateGridNota() {
    subView.updateGrid(listEmailRecebido())
  }
  
  private fun listEmailRecebido() = EmailDB.listEmailRecebidos()
}

interface IEmailRecebido {
  fun updateGrid(listEmailRecebido: List<EmailDB>)
}