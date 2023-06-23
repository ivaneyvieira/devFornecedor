package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class FornecedorNota(
    val loja: Int,
    val ni: Int,
    val nf: String?,
    val emissao: LocalDate?,
    val entrada: LocalDate?,
    val valorNota: Double?,
    val obs: String?,
    val vencimento: LocalDate?,
    val situacao: String?,
    val obsParcela: String?,
    val quantAnexo: Int,
) {
  fun findAnexos(): List<NFFile> {
    return saci.selectFile(this)
  }

  fun delAnexo(bean: NFFile) {
    saci.deleteFile(bean)
  }

  fun addAnexo(fileName: String?, bytes: ByteArray) {
    fileName ?: return
    saci.insertFile(
        NFFile(
            storeno = 77,
            pdvno = 7777,
            xano = ni,
            date = LocalDate.now(),
            nome = fileName,
            file = bytes,
        )
    )
  }

  companion object {
    fun findByFornecedor(filtro: FiltroFornecedorNota): List<FornecedorNota> {
      return saci.fornecedorNotas(filtro)
    }
  }
}

data class FiltroFornecedorNota(val vendno: Int, val loja: Int, val query: String)