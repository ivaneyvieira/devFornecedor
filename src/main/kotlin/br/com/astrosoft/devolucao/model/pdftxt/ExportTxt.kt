package br.com.astrosoft.devolucao.model.pdftxt

import com.lordcodes.turtle.shellRun
import org.apache.commons.lang.RandomStringUtils
import java.io.File
import kotlin.io.path.deleteExisting

object ExportTxt {
  fun parsePdfFile(pdf: String, txt: String) {
    try {
      val output = shellRun("/usr/bin/pdftotext4", listOf("-table", "-enc", "UTF-8", pdf, txt))
      println(output)
    }catch (e: Throwable){
      println(e.message)
    }
  }

  fun toTxt(pdf: ByteArray) : ByteArray{
    val randonName = RandomStringUtils.randomAlphanumeric(8)
    val pdfPath = "/tmp/$randonName.pdf"
    val txtPath = "/tmp/$randonName.txt"
    File(pdfPath).writeBytes(pdf)
    parsePdfFile(pdfPath, txtPath)
    val out = File(txtPath).readBytes()
    File(txtPath).toPath().deleteExisting()
    File(pdfPath).toPath().deleteExisting()
    return out
  }
}

fun main() {
  ExportTxt.parsePdfFile("/tmp/pdf/pedido01.pdf", "/tmp/pdf/pedido01.txt")
}