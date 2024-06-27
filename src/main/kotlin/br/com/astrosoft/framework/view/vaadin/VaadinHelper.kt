package br.com.astrosoft.framework.view.vaadin

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

@VaadinDsl
fun (@VaadinDsl HasComponents).verticalBlock(
  label: String? = null,
  block: (@VaadinDsl VerticalLayout).() -> Unit = {}
): VerticalLayout {
  val layout: VerticalLayout = VerticalLayout().apply {
    this.isPadding = false
    this.isSpacing = false
    this.isMargin = false
    this.isExpand = true
    content { align(left, top) }
    if (label != null) {
      h5(label)
      hr()
    }
  }
  return init(layout, block)
}

@VaadinDsl
fun (@VaadinDsl HasComponents).horizontalBlock(
  block: (@VaadinDsl HorizontalLayout).() -> Unit = {}
): HorizontalLayout {
  val layout: HorizontalLayout = HorizontalLayout().apply {
    this.isPadding = false
    this.isSpacing = false
    this.isMargin = false
    this.isExpand = true
    content { align(left, top) }
  }
  return init(layout, block)
}