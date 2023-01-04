package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.label
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import java.io.InputStream

class SubWindowXlsx(chave: String, bytesBoletos: ByteArray) : Dialog() {
  init {
    width = "300px"
    height = "200px"
    val timeNumber = System.currentTimeMillis()
    verticalLayout {
      isPadding = false
      label ("Baixar a planilha")
      horizontalLayout {
        lazyDownloadButtonXlsx("Planilha", chave) {
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