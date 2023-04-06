package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.model.MailGMail
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabEmailRecebidoViewModel(val viewModel: Devolucao66ViewModel) : IEmailView {
  private val subView
    get() = viewModel.view.tabEmailRecebido

  override fun updateView() = viewModel.exec {
    subView.updateGrid(listEmailRecebido())
  }

  private fun listEmailRecebido() = EmailDB.listEmailRecebidos().sortedWith(compareByDescending<EmailDB> {
    it.data
  }.thenByDescending {
    it.hora
  })

  override fun listEmail(fornecedor: Fornecedor?): List<String> = emptyList()

  override fun enviaEmail(gmail: EmailGmail, notas: List<NotaSaida>) = viewModel.exec {
    val mail = MailGMail()
    val enviadoComSucesso = mail.sendMail(gmail.email, gmail.assunto, gmail.msgHtml, emptyList())
    if (enviadoComSucesso) {
      val idEmail = EmailDB.newEmailId()
      gmail.salvaEmail(idEmail)
    } else fail("Erro ao enviar e-mail")
  }
}

interface ITabEmailRecebido : ITabView {
  fun updateGrid(itens: List<EmailDB>)
}