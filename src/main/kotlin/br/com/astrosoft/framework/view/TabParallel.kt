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
import kotlinx.coroutines.*
import kotlin.math.roundToInt

open class TabParallel {
  private var job: Job? = null
  private var scope = CoroutineScope(Dispatchers.IO)
  private var currentUI: UI? = null
  private val progressBar = ProgressBar()
  private val progressBarLabel = Div()
  protected val progressBarLayout = HorizontalLayout().apply {
    isVisible = false
    isMargin = false
    alignItems = Alignment.END
    button("Cancelar") {
      isVisible = false
      onLeftClick {
        access {
          this@apply.isVisible = false
          scope.launch {
            job?.cancelAndJoin()
          }
        }
      }
    }

    verticalLayout {
      isMargin = false
      add(progressBarLabel, progressBar)
    }
  }

  protected fun launch(run: () -> Unit) {
    currentUI = UI.getCurrent()

    job = scope.launch {
      try {
        access {
          progressBar.value = 0.0
          progressBarLayout.isVisible = true
        }
        run()
        access {
          progressBarLayout.isVisible = false
        }
      } catch (e: CancellationException) {
        e.printStackTrace()
      }
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
    }
  }
}