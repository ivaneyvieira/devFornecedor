package br.com.astrosoft.framework.view

import com.vaadin.flow.component.UI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class TabParallel {
  protected var currentUI: UI? = null

  fun launch(run: () -> Unit) {
    currentUI = UI.getCurrent()

    val job = GlobalScope.launch {
      run()
    }
  }

  fun access(run: () -> Unit) {
    currentUI?.access {
      run()
    }
  }
}