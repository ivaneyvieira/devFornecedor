package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class Fornecedor(
  val custno: Int,
  val fornecedor: String,
  val vendno: Int,
  val notas: List<NotaSaida>
                ) {
  fun listRepresentantes() = saci.representante(vendno)
}