package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.view.SubWindowForm

class DlgEnviaEmail<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) {
  fun enviaEmail(notas: List<NotaSaida>) {
    val nota = notas.firstOrNull() ?: return
    val form = SubWindowForm("DEV FORNECEDOR: ${nota.fornecedor}") {
      FormEmail(viewModel, notas)
    }
    form.open()
  }
}