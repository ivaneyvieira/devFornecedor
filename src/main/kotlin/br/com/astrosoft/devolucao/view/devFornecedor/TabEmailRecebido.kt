package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.view.emailAssunto
import br.com.astrosoft.devolucao.view.emailData
import br.com.astrosoft.devolucao.view.emailEmail
import br.com.astrosoft.devolucao.view.emailHora
import br.com.astrosoft.devolucao.view.emailTipo
import br.com.astrosoft.devolucao.viewmodel.devolucao.EmailRecebidoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.IEmailRecebido
import br.com.astrosoft.framework.view.TabPanelGrid
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class TabEmailRecebido(val viewModel: EmailRecebidoViewModel):
  TabPanelGrid<EmailDB>(EmailDB::class), IEmailRecebido {
  override fun HorizontalLayout.toolBarConfig() {
  
  }
  
  override fun Grid<EmailDB>.gridPanel() {
    emailData()
    emailHora()
    emailAssunto()
    emailEmail()
  }
  
  override val label: String
    get() = "E-mails Recebidos"
  
  override fun updateComponent() {
    viewModel.updateGridNota()
  }
}