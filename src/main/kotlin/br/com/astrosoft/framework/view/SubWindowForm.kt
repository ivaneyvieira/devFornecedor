package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.content
import com.github.mvysny.karibudsl.v10.h4
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.label
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.VaadinIcon

class SubWindowForm(labelTitle: String,
                    val toolBar: HasComponents.() -> Unit = {},
                    val blockForm: () -> Component):
  Dialog() {
  init {
    width = "100%"
    height = "100%"
    
    verticalLayout {
      content {align(stretch, top)}
      isPadding = false
      horizontalLayout {
        content {align(left, baseline)}
        button("Fechar") {
          icon = VaadinIcon.CLOSE.create()
          onLeftClick {
            close()
          }
        }
        toolBar()
      }
      horizontalLayout {
        isSpacing= true
        isPadding = false
        setWidthFull()
        labelTitle.split("|")
          .forEach {linha ->
            h4(linha){
              isExpand = true
            }
          }
      }
      
      addAndExpand(blockForm())
    }
    isCloseOnEsc = true
  }
}

