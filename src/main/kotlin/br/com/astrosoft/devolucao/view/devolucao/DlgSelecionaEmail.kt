package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailAssunto
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailData
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailEmail
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailHora
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailTipo
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnButton
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon

class DlgSelecionaEmail<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) {
  fun selecionaEmail(nota: NotaSaida, emails: List<EmailDB>) {
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}") {
      createGridEmail(nota, emails)
    }
    form.open()
  }

  private fun createGridEmail(nota: NotaSaida, emails: List<EmailDB>): Grid<EmailDB> {
    val gridDetail = Grid(EmailDB::class.java, false)
    val lista = emails + nota.listaEmailRecebidoNota()
    return gridDetail.apply {
      addThemeVariants()
      isMultiSort = false // setSelectionMode(MULTI)
      setItems(lista.sortedWith(compareByDescending<EmailDB> { it.data }.thenByDescending { it.hora }))

      addColumnButton(VaadinIcon.EDIT, "Edita e-mail", "Edt") { emailEnviado ->
        editEmail(nota, emailEnviado)
      }

      emailData()
      emailHora()
      emailAssunto()
      emailTipo()
      emailEmail()
    }
  }

  private fun editEmail(nota: NotaSaida, emailEnviado: EmailDB?) {
    val form = SubWindowForm("DEV FORNECEDOR: ${nota.fornecedor}") {
      FormEmail(viewModel, listOf(nota), emailEnviado)
    }
    form.open()
  }
}