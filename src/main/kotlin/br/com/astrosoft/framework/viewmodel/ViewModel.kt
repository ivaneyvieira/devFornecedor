package br.com.astrosoft.framework.viewmodel

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.log

abstract class ViewModel<V : IView>(val view: V) {
  fun exec(block: () -> Unit) = exec(view, block)
  protected abstract fun listTab(): List<ITabView>

  fun tabsAuthorized() = listTab().filter {
    val user = AppConfig.user ?: return@filter false
    it.isAuthorized(user)
  }
}

fun exec(view: IView, block: () -> Unit) {
  try {
    block()
  } catch (e: EViewModelFail) {
    view.showError(e.message ?: "Erro generico")
    log?.error(e.toString())
    throw e
  }
}

interface IViewModelUpdate {
  fun updateView()
}

fun fail(message: String): Nothing {
  throw EViewModelFail(message)
}

interface IView {
  fun showError(msg: String)
  fun showWarning(msg: String)
  fun showInformation(msg: String)
}