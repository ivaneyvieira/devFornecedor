package br.com.astrosoft.framework.model

interface IUser {
  var ativo: Boolean
  var login: String
  val admin: Boolean
  var senha: String
}