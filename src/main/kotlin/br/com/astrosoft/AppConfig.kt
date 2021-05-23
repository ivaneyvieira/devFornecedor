package br.com.astrosoft

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.devolucao.view.devolucao.Devolucao01View
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.session.SecurityUtils
import br.com.astrosoft.framework.view.ViewUtil

object AppConfig {
  val mainClass = Devolucao01View::class
  val version = ViewUtil.versao
  const val appName = "devFornecedor"
  const val commpany = "Engecopi"
  const val title = "Devolução Fornecedor"
  const val shortName = "Devolução"
  const val iconPath = "icons/logo.png"
  val user get() = SecurityUtils.userDetails
  val isAdmin get() = user?.admin == true
  fun findUser(username: String?): IUser? = saci.findUser(username)
}
