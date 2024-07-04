package br.com.astrosoft.framework.view.vaadin

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.model.Config.user
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder

class FormUsuario(val user: UserSaci, val init : FormUsuario.() -> Unit) : FormLayout() {
  val binder: Binder<UserSaci> = Binder(UserSaci::class.java)

  init {
    binder.bean = user
    init()
  }

  val userSaci: UserSaci
    get() = binder.bean
}