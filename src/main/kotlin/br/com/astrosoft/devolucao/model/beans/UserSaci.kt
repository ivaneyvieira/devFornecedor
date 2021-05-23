package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.IUser
import kotlin.math.pow
import kotlin.reflect.KProperty

class UserSaci : IUser {
  var no: Int = 0
  var name: String = ""
  override var login: String = ""
  override var senha: String = ""
  var bitAcesso: Int = 0
  var storeno: Int = 0
  var prntno: Int = 0
  var impressora: String = ""
  override var ativo by DelegateAuthorized(0)
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
  var nota01Coleta by DelegateAuthorized(11)
  var remessaConserto by DelegateAuthorized(12)
  var ajusteGarantia by DelegateAuthorized(13)
  var notaFinanceiro by DelegateAuthorized(14)
  val menuDevolucao01 = nota01 || pedido || nota01Coleta || remessaConserto || ajusteGarantia || notaFinanceiro
  val menuDevolucao66 = nota66 || nota66Pago || entrada || emailRecebido
  val menuRecebimento = notaPendente
  val menuAgenda = agendaAgendada || agendaNaoAgendada || agendaRecebida
  override val admin
    get() = login == "ADM"

  companion object {
    fun findAll(): List<UserSaci> {
      return saci.findAllUser().filter { it.ativo }
    }

    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }

    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login)
    }
  }
}

class DelegateAuthorized(numBit: Int) {
  private val bit = 2.toDouble().pow(numBit).toInt()

  operator fun getValue(thisRef: UserSaci?, property: KProperty<*>): Boolean {
    thisRef ?: return false
    return (thisRef.bitAcesso and bit) != 0 || thisRef.admin
  }

  operator fun setValue(thisRef: UserSaci?, property: KProperty<*>, value: Boolean?) {
    thisRef ?: return
    val v = value ?: false
    thisRef.bitAcesso = when {
      v    -> thisRef.bitAcesso or bit
      else -> thisRef.bitAcesso and bit.inv()
    }
  }
}


