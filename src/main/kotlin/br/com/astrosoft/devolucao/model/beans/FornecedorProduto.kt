package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class FornecedorProduto(
  val vendno: Int,
  val custno: Int?,
  val nomeFornecedor: String,
  val data: LocalDate?,
  val invno: Int?,
  val nota: String?,
  val quantAnexo: Int,
                       ) {
  fun getDemanda(): AgendaDemanda {
    val filter = FilterAgendaDemanda(pesquisa = "", concluido = null, vendno = vendno)
    return saci.selectAgendaDemanda(filter).firstOrNull() ?: createDemanda()
  }

  private fun createDemanda(): AgendaDemanda {
    val agenda =
      AgendaDemanda(
        date = LocalDate.now(),
        titulo = "",
        conteudo = "",
        vendno = vendno,
        destino = "",
        origem = "",
                   )
    agenda.save()
    return agenda
  }

  companion object {
    fun findAll(filtro: String): List<FornecedorProduto> {
      return saci.fornecedorProduto(filtro)
    }
  }
}