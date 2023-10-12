package br.com.astrosoft.devolucao.model.pdftxt

import com.lordcodes.turtle.shellRun
import org.apache.commons.lang.RandomStringUtils
import java.io.File
import kotlin.io.path.deleteExisting

object ExportTxt {
  fun parsePdfFile(pdf: String, txt: String, fixedChar: Int) {
    try {
      val parametros =
          listOf("-table", "-fixed", "$fixedChar", "-nodiag", "-nopgbrk", "-clip", "-enc", "UTF-8", pdf, txt)
      val output = shellRun("/usr/bin/pdftotext4", parametros)
      println(output)
    } catch (e: Throwable) {
      println(e.message)
    }
  }

  fun toTxt(pdf: ByteArray, fixedChar: Int): ByteArray {
    val randonName = RandomStringUtils.randomAlphanumeric(8)
    val pdfPath = "/tmp/$randonName.pdf"
    val txtPath = "/tmp/$randonName.txt"
    File(pdfPath).writeBytes(pdf)
    parsePdfFile(pdf = pdfPath, txt = txtPath, fixedChar = fixedChar)
    val out = File(txtPath).readBytes()
    File(txtPath).toPath().deleteExisting()
    File(pdfPath).toPath().deleteExisting()
    return out
  }
}

fun main() {
  ExportTxt.parsePdfFile(pdf = "/tmp/pdf/pedido01.pdf", txt = "/tmp/pdf/pedido01.txt", fixedChar = 3)
}