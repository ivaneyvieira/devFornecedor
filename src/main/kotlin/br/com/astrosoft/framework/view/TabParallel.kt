package br.com.astrosoft.framework.view

import com.vaadin.flow.component.UI

open class TabParallel2 {
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