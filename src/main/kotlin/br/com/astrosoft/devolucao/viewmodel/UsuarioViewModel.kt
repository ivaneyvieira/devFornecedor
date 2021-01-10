package br.com.astrosoft.devolucao.viewmodel

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IUsuarioView
import br.com.astrosoft.framework.viewmodel.UserViewModel

class UsuarioViewModel(view: IUsuarioView): UserViewModel<UserSaci, IUsuarioView>(view) {
  override fun listTab(): List<ITabView> = emptyList()

  override fun findAllUser() = UserSaci.findAll()
  
  override fun findUser(login: String) = UserSaci.findUser(login)
  
  override fun addUser(user: UserSaci) {
    UserSaci.updateUser(user)
  }
  
  override fun updateUser(user: UserSaci) {
    UserSaci.updateUser(user)
  }
  
  override fun deleteUser(user: UserSaci) {
    UserSaci.updateUser(user)
  }
}

