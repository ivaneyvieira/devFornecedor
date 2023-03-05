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
  val nomeFantasia: String,
  val cnpj: String,
  val uf: String,
                       ) {
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

  fun findAnexos(): List<NFFile> {
    return saci.selectFile(this)
  }

  fun delAnexo(bean: NFFile) {
    saci.deleteFile(bean)
  }

  fun addAnexo(fileName: String?, bytes: ByteArray) {
    fileName ?: return
    saci.insertFile(NFFile(
      storeno = 88,
      pdvno = 8888,
      xano = vendno,
      date = LocalDate.now(),
      nome = fileName,
      file = bytes,
                          ))
  }

  companion object {
    fun findAll(filtro: String): List<FornecedorProduto> {
      return saci.fornecedorProduto(filtro)
    }
  }
}