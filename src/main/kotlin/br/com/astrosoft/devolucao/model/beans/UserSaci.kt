package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.IUser
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.reflect.KProperty

class UserSaci: IUser {
  var no: Int = 0
  var name: String = ""
  override var login: String = ""
  override var senha: String = ""
  var bitAcesso: Int = 0
  var storeno: Int = 0
  var prntno: Int = 0
  var impressora: String = ""
  
  //Outros campos
  val permissoes
    get() = Permissoes(this)
  override val admin
    get() = login == "ADM"
  override var ativo: Boolean
    get() = permissoes.ativo
    set(value) {
      permissoes.ativo = value
    }
  
  companion object {
    fun findAll(): List<UserSaci> {
      return saci.findAllUser().filter {it.permissoes.ativo}
    }
    
    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }
    
    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login)
    }
  }
}

class Permissoes(val user: UserSaci) {
  var ativo by DelegateAuthorized(0)
  var nota01 by DelegateAuthorized(1)
  var nota66 by DelegateAuthorized(2)
  var pedido by DelegateAuthorized(3)
  var nota66Pago by DelegateAuthorized(4)
  var emailRecebido by DelegateAuthorized(5)
  var notaPendente by DelegateAuthorized(6)
  var agendaAgendada by DelegateAuthorized(7)
  var agendaNaoAgendada by DelegateAuthorized(8)
  var agendaRecebida by DelegateAuthorized(9)
  var entrada by DelegateAuthorized(10)
  val menuDevolucao = nota01 || nota66 || pedido || nota66Pago || emailRecebido || entrada
  val menuRecebimento = notaPendente
  val menuAgenda = agendaAgendada || agendaNaoAgendada || agendaRecebida
}

class DelegateAuthorized(private val numBit: Int) {
  private val bit = 2.toDouble().pow(numBit).toInt()
  
  operator fun getValue(thisRef: Permissoes?, property: KProperty<*>): Boolean {
    thisRef ?: return false
    return (thisRef.user.bitAcesso and bit) != 0 || thisRef.user.admin
  }
  
  operator fun setValue(thisRef: Permissoes?, property: KProperty<*>, value: Boolean?) {
    thisRef ?: return
    value ?: return
    thisRef.user.bitAcesso = if(value) thisRef.user.bitAcesso or bit
    else thisRef.user.bitAcesso and bit.inv()
  }
}

class Filme(val nome: String, val ano : Int, val duracao: Int){

}
