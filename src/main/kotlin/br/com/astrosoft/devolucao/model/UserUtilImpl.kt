package br.com.astrosoft.devolucao.model

import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.model.IUserUtil

class UserUtilImpl : IUserUtil {
  override fun findUser(username: String): IUser? = saci.findUser(username).firstOrNull()
}