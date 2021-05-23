package br.com.astrosoft.framework.session

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.IUser

object SecurityUtils {
  fun login(username: String?, password: String?): Boolean {
    val user = AppConfig.findUser(username) ?: return false
    return if (user.senha == password) {
      Session[IUser::class] = user
      true
    }
    else {
      Session.current.close()
      false
    }
  }

  val isUserLoggedIn: Boolean
    get() = Session[IUser::class] != null

  val userDetails: IUser?
    get() = Session[IUser::class]
}