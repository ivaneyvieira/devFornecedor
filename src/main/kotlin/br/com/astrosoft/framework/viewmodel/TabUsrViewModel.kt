package br.com.astrosoft.framework.viewmodel

import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.UserSaci

abstract class TabUsrViewModel(val vm: ViewModel<*>) {
  abstract val subView: ITabUser

  fun findLoja(storeno: Int): Loja? {
    val lojas = Loja.allLojas()
    return lojas.firstOrNull { it.no == storeno }
  }

  fun findAllLojas(): List<Loja> {
    return Loja.allLojas()
  }

  fun modificarUsuario() = vm.exec {
    val usuario = subView.selectedItem() ?: fail("Usuário não selecionado")
    subView.formUpdUsuario(usuario)
  }

  fun adicionaUsuario() = vm.exec {
    subView.formAddUsuario()
  }

  abstract fun UserSaci.desative()
  abstract fun UserSaci.isActive(): Boolean
  abstract fun UserSaci.update(usuario: UserSaci)

  fun removeUsuario() = vm.exec {
    val usuario = subView.selectedItem() ?: fail("Usuário não selecionado")
    vm.view.showQuestion("Deseja remover o usuário ${usuario.name} da reposição?") {
      usuario.desative()
      UserSaci.updateUser(usuario)
      updateView()
    }
  }

  fun updateView() = vm.exec {
    val filter = subView.filter()
    val usuarios = usuarios().filter {
      filter == "" ||
      it.no.toString() == filter ||
      it.name .contains(filter, ignoreCase = true) ||
      it.login.startsWith(filter, ignoreCase = true)
    }
    subView.updateUsuarios(usuarios)
  }

  private fun usuarios(): List<UserSaci> {
    val usuarios = UserSaci.findAll().filter {
      it.isActive()
    }
    return usuarios
  }

  fun updUser(usuario: UserSaci) {
    println("Pedido pendente: ${usuario.pedidoPendente}")
    println("Pedido editor: ${usuario.pedidoEditor}")
    println("Pedido finalizado: ${usuario.pedidoFinalizado}")

    UserSaci.updateUser(usuario)
    updateView()
  }

  fun addUser(userSaci: UserSaci) {
    val user = UserSaci.findUser(userSaci.login)
    user.forEach { u ->
      u.ativo = true
      u.update(userSaci)
      UserSaci.updateUser(u)
    }
    updateView()
  }
}

interface ITabUser : ITabView {
  fun filter(): String
  fun updateUsuarios(usuarios: List<UserSaci>)
  fun formAddUsuario()
  fun selectedItem(): UserSaci?
  fun formUpdUsuario(usuario: UserSaci)
}