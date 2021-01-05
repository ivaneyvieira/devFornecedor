package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class Fornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val email: String,
  val notas: List<NotaSaida>
                ) {
  fun listRepresentantes() = saci.representante(vendno)
  
  val ultimaData = notas.maxOf {nota ->
    if(nota.tipo == "PED") nota.dataPedido else nota.dataNota
  }
  
  fun listEmail(): List<String> {
    val list = listRepresentantes().map {
      it.email
    } + email
    
    return list.distinct()
      .filter {
        it != ""
      }
      .sorted()
  }
}