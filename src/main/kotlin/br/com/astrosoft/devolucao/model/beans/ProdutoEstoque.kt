package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.lpad
import java.time.LocalDate

class ProdutoEstoque(
  var loja: Int?,
  var lojaSigla: String?,
  var prdno: String?,
  var codigo: Int?,
  var descricao: String?,
  var unidade: String?,
  var grade: String?,
  var embalagem: Int?,
  var qtdEmbalagem: Double?,
  var locApp: String?,
  var codForn: Int,
  var fornecedor: String,
  var saldo: Int?,
  var dataConferencia: LocalDate?,
  var valorConferencia: Int?
) {

  val codigoStr
    get() = this.codigo?.toString() ?: ""

  fun update() {
    saci.updateProdutoEstoque(this)
  }

  companion object {
    fun findProdutoEstoque(filter: FiltroProdutoEstoque): List<ProdutoEstoque> {
      return saci.findProdutoEstoque(filter)
    }
  }
}

data class FiltroProdutoEstoque(
  val pesquisa: String,
  val codigo: Int,
  val grade: String,
  val caracter: ECaracter,
  val localizacao: String?,
  val fornecedor: String,
  val centroLucro: Int = 0,
  val estoque: EEstoque = EEstoque.TODOS,
  val saldo: Int = 0,
  val inativo: EInativo
) {
  val prdno = if (codigo == 0) "" else codigo.toString().lpad(16, " ")
}

enum class ECaracter(val value: String, val descricao: String) {
  SIM("S", "Sim"),
  NAO("N", "Não"),
  TODOS("T", "Todos"),
}

enum class EEstoque(val value: String, val descricao: String) {
  MENOR("<", "<"),
  MAIOR(">", ">"),
  IGUAL("=", "="),
  TODOS("T", "Todos"),
}

enum class EInativo(val codigo: String?, val descricao: String) {
  NAO("N", "Não"), SIM("S", "Sim"), TODOS("T", "Todos")
}