package br.com.astrosoft

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.spring.SecurityUtils
import br.com.astrosoft.framework.view.ViewUtil

object AppConfig {
  val version = ViewUtil.versao
  const val commpany = "Engecopi"
  const val title = "Devolução Fornecedor"
  const val shortName = "Devolução"
  const val iconPath = "icons/logo.png"
  val userDetails get() = SecurityUtils.userDetails
  val user get() = userDetails?.userSaci
  val isAdmin get() = user?.admin == true
  fun findUser(username: String?): IUser? = saci.findUser(username)
}
