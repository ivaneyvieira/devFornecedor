package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.MonitorHandler
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.progressbar.ProgressBar
import kotlin.math.roundToInt

open class TabParallel {
  private var currentUI: UI? = null

  protected fun launch(run: () -> Unit) {
    run()
  }

  protected fun access(run: () -> Unit) {
    currentUI?.access {
      run()
    }
  }
}