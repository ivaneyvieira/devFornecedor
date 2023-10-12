package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NFFile(
  val storeno: Int,
  val pdvno: Int,
  val xano: Int,
  val date: LocalDate,
  var nome: String,
  var file: ByteArray
) {
  fun insert() = saci.insertFile(this)

  fun update() = saci.updateFile(this)

  fun delete() = saci.deleteFile(this)

  fun saveFile() {
    saci.insertFile(this)
  }

  companion object {
    fun new(nota: NotaSaida, fileName: String, bytes: ByteArray): NFFile {
      return when (nota.tipo) {
        "PED" -> NFFile(
          storeno = nota.loja,
          pdvno = 9999,
          xano = nota.pedido,
          date = LocalDate.now(),
          nome = fileName,
          file = bytes
        )

        else  -> NFFile(
          storeno = nota.loja,
          pdvno = nota.pdv,
          xano = nota.transacao,
          date = LocalDate.now(),
          nome = fileName,
          file = bytes
        )
      }
    }
  }
}