package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.server.StreamResource

class SubWindowView(filename: String, bytesBoletos: ByteArray) : Dialog() {
  init {
    width = "100%"
    height = "100%"
    val resourcePDF = StreamResource(filename, ConverteByte(bytesBoletos))
    verticalLayout {
      isPadding = false
      horizontalLayout {
        add(Anchor(resourcePDF, "Download"))
        button("Fechar") {
          icon = VaadinIcon.CLOSE.create()
          onLeftClick {
            close()
          }
        }
      }

      addAndExpand(PDFViewer(resourcePDF))
    }
    isCloseOnEsc = true
  }
}
