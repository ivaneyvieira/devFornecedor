package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.view.emailAssunto
import br.com.astrosoft.devolucao.view.emailData
import br.com.astrosoft.devolucao.view.emailEmail
import br.com.astrosoft.devolucao.view.emailHora
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabEmailRecebido
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabEmailRecebidoViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class TabEmailRecebido(val viewModel: TabEmailRecebidoViewModel):
  TabPanelGrid<EmailDB>(EmailDB::class), ITabEmailRecebido {
  override fun HorizontalLayout.toolBarConfig() {}
  
  override fun Grid<EmailDB>.gridPanel() {
    addColumnButton(EDIT, "Edita e-mail", "Edt") {emailEnviado ->
      editEmail(emailEnviado)
    }
    emailData()
    emailHora()
    emailAssunto()
    emailEmail()
  }
  
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.emailRecebido == true
  }
  
  override val label: String
    get() = "E-mails Recebidos"
  
  override fun updateComponent() {
    viewModel.updateGridNota()
  }
  
  private fun editEmail(emailEnviado: EmailDB?) {
    val form = SubWindowForm("E-MAIL RECEBIDO") {
      FormEmail(viewModel, emptyList(), emailEnviado)
    }
    form.open()
  }
}