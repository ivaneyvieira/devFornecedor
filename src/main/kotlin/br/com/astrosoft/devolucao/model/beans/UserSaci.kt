package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import kotlin.math.pow

class UserSaci {
  var no: Int = 0
  var name: String = ""
  var login: String = ""
  var senha: String = ""
  var bitAcesso: Int = 0
  var storeno: Int = 0
  var prntno: Int = 0
  var impressora: String = ""
  
  //Outros campos
  var ativo
    get() = (bitAcesso and BIT_ATIVO) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_ATIVO
      else bitAcesso and BIT_ATIVO.inv()
    }
  var nota01
    get() = (bitAcesso and BIT_NOTA01) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_NOTA01
      else bitAcesso and BIT_NOTA01.inv()
    }
  var nota66
    get() = (bitAcesso and BIT_NOTA66) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_NOTA66
      else bitAcesso and BIT_NOTA66.inv()
    }
  var pedido
    get() = (bitAcesso and BIT_PEDIDO) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_PEDIDO
      else bitAcesso and BIT_PEDIDO.inv()
    }
  val admin
    get() = login == "ADM"
  
  companion object {
    private val BIT_ATIVO = 2.pow(0)
    private val BIT_NOTA01 = 2.pow(1)
    private val BIT_NOTA66 = 2.pow(2)
    private val BIT_PEDIDO = 2.pow(3)
    
    fun findAll(): List<UserSaci>? {
      return saci.findAllUser()
        .filter {it.ativo}
    }
    
    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }
    
    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login)
    }
  }
}

fun Int.pow(e: Int): Int = this.toDouble()
  .pow(e)
  .toInt()