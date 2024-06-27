package br.com.astrosoft.framework.view.vaadin

import br.com.astrosoft.framework.view.SubWindowPDF
import com.github.mvysny.karibudsl.v10.html
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div

object DialogHelper {
  fun showForm(caption: String, form: FormLayout, runConfirm: (() -> Unit)) {
    ConfirmDialog().apply {
      this.setHeader(caption)
      this.setText(form)
      this.setConfirmText("Confirma")
      this.addConfirmListener {
        runConfirm()
      }
      this.width = form.width
      form.setWidthFull()
      this.setCancelable(true)
      this.setCancelText("Cancela")
      this.open()
    }
  }

  fun showError(msg: String) {
    ConfirmDialog().apply {
      this.setHeader("Erro do aplicativo")
      this.setText(msg)
      this.setConfirmText("OK")
      this.open()
    }
  }

  fun showWarning(msg: String) {
    ConfirmDialog().apply {
      this.setHeader("Aviso")
      this.setText(msg)
      this.setConfirmText("OK")
      this.open()
    }
  }

  fun showInformation(msg: String, title: String = "Informação") {
    ConfirmDialog().apply {
      this.setHeader(title)
      this.setText(Div().apply {
        this.html(msg)
      })
      this.setConfirmText("OK")
      this.open()
    }
  }

  fun showReport(chave: String, report: ByteArray) {
    SubWindowPDF(chave, report).open()
  }

  fun showQuestion(msg: String, execYes: () -> Unit) {
    showQuestion(msg, execYes) {}
  }

  fun showQuestion(msg: String, execYes: () -> Unit, execNo: () -> Unit) {
    val dialog = ConfirmDialog()
    dialog.setHeader("Confirmação")
    dialog.setText(msg)

    dialog.setCancelable(true)
    dialog.setCancelText("Não")
    dialog.addCancelListener {
      execNo()
    }

    dialog.setConfirmText("Sim")
    dialog.setConfirmButtonTheme("error primary")
    dialog.addConfirmListener {
      execYes()
    }
    dialog.open()
  }
}