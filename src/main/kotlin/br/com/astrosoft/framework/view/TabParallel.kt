package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.MonitorHandler
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.progressbar.ProgressBar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

open class TabParallel {
  private var currentUI: UI? = null
  private val progressBar = ProgressBar()
  private val progressBarLabel = Div()
  protected val progressBarLayout = VerticalLayout().apply {
    isVisible = false
    add(progressBarLabel, progressBar)
  }

  protected fun launch(run: () -> Unit) {
    currentUI = UI.getCurrent()

    val job = GlobalScope.launch {
      run()
    }
  }

  protected fun access(run: () -> Unit) {
    currentUI?.access {
      run()
    }
  }

  protected val updateProgress: MonitorHandler = { text: String, pos: Int, total: Int ->
    access {
      progressBar.min = 0.00
      progressBar.max = total.toDouble()
      progressBar.value = pos.toDouble()
      val procentagem = ((pos.toDouble() / total.toDouble()) * 100).roundToInt()
      progressBarLabel.text = "$text $procentagem %"
      progressBarLayout.isVisible = procentagem < 100
    }
  }
}