package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.viewmodel.devolucao.IEmailView
import br.com.astrosoft.framework.util.htmlToText
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField

class FormEmail(val viewModel: IEmailView, notas: List<NotaSaida>, emailEnviado: EmailDB? = null) : VerticalLayout() {
  private lateinit var chkPlanilha: Checkbox
  private lateinit var edtAssunto: TextField
  private var rteMessage: TextArea
  private lateinit var chkAnexos: Checkbox
  private lateinit var chkRelatorio: Checkbox
  private lateinit var chkRelatorioResumido: Checkbox
  private lateinit var cmbEmail: ComboBox<String>
  private var gmail: EmailGmail
    get() = EmailGmail(
      email = cmbEmail.value ?: "",
      assunto = edtAssunto.value ?: "",
      msg = { rteMessage.value ?: "" },
      msgHtml = rteMessage.value ?: "",
      planilha = if (chkPlanilha.value) "S" else "N",
      relatorio = if (chkRelatorio.value) "S" else "N",
      relatorioResumido = if (chkRelatorioResumido.value) "S" else "N",
      anexos = if (chkAnexos.value) "S" else "N",
      messageID = ""
    )
    set(value) {
      cmbEmail.value = value.email
      edtAssunto.value = value.assunto
      rteMessage.value = htmlToText(value.msg())
      chkPlanilha.value = value.planilha == "S"
      chkRelatorio.value = value.relatorio == "S"
      chkRelatorioResumido.value = value.relatorioResumido == "S"
      chkAnexos.value = value.anexos == "S"
    }

  init {
    val fornecedor = NotaSaida.findFornecedores("").firstOrNull { it.notas.containsAll(notas) }
    rteMessage = richEditor()
    setSizeFull()
    horizontalLayout {
      setWidthFull()
      cmbEmail = comboBox("E-Mail") {
        this.width = "400px"
        this.isAllowCustomValue = true
        setItems(viewModel.listEmail(fornecedor))
        addCustomValueSetListener { event ->
          this.value = event.detail
        }
      }
      edtAssunto = textField("Assunto") {
        this.isExpand = true
      }
      chkRelatorio = checkBox("Relatório")
      chkRelatorioResumido = checkBox("Resumido")
      chkPlanilha = checkBox("Planilha")
      chkAnexos = checkBox("Anexos")

      button("Enviar") { // val numerosNota = notas.joinToString(separator = " ") {it.nota}
        onLeftClick {
          viewModel.enviaEmail(gmail, notas)
        }
      }
    }
    rteMessage.width = "100%"

    addAndExpand(rteMessage)
    emailEnviado?.let { email ->
      gmail = email.emailBean()
    }
  }

  private fun richEditor(): TextArea {
    return TextArea()
  }
}