package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.VaadinIcon

class SubWindowXlsx(filename: String, bytesBoletos: ByteArray) : Dialog() {
  init {
    verticalLayout {
      isPadding = false
      h4("Baixar a planilha")
      br()
      horizontalLayout {
        lazyDownloadButtonXlsx("Planilha", filename) {
          bytesBoletos
        }
        button("Fechar") {
          icon = VaadinIcon.CLOSE.create()
          onLeftClick {
            close()
          }
        }
      }
    }
    isCloseOnEsc = true
  }
}