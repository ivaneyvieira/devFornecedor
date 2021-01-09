package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class Agenda(
  val loja: Int,
  val data: LocalDate?,
  val hora: String,
  val recebedor: String,
  val invno: String,
  val fornecedor: Int,
  val abreviacao: String,
  val emissao: LocalDate?,
  val nf: String,
  val volume: String,
  val total: Double,
  val transp: Int,
  val nome: String,
  val pedido: Int
            ){
  
  companion object{
    fun listaAgenda(agendado : Boolean) = saci.listaAgenda(agendado)
  }
}