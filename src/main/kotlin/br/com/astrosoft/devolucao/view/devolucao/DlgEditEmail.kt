package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabEmailRecebidoViewModel
import br.com.astrosoft.framework.view.SubWindowForm

class DlgEditEmail(val viewModel: TabEmailRecebidoViewModel) {
  fun editEmail(emailEnviado: EmailDB?) {
    val form = SubWindowForm("E-MAIL RECEBIDO") {
      FormEmail(viewModel, emptyList(), emailEnviado)
    }
    form.open()
  }
}