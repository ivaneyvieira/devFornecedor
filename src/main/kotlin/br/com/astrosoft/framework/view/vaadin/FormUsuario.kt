package br.com.astrosoft.framework.view.vaadin

import br.com.astrosoft.devolucao.model.beans.UserSaci
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder

class FormUsuario(val userSaci: UserSaci, val init : FormUsuario.() -> Unit) : FormLayout() {
  val binder: Binder<UserSaci> = Binder(UserSaci::class.java)

  init {
    binder.bean = userSaci
    init()
  }

}