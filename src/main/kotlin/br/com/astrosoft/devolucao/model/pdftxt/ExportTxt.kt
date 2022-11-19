package br.com.astrosoft.devolucao.model.pdftxt

import io.github.jonathanlink.PDFLayoutTextStripper
import org.apache.pdfbox.io.RandomAccessFile
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.FileOutputStream

object ExportTxt {
  /*
    fun parsePdf(pdf: ByteArray) : ByteArray{
      val reader = PdfReader(pdf)
      val parser = PdfReaderContentParser(reader)
      val baos = ByteArrayOutputStream()
      val out = PrintWriter(baos)
      var strategy: TextExtractionStrategy
      for (i in 1..reader.numberOfPages) {
        strategy = parser.processContent(i, SimpleTextExtractionStrategy())
        out.println(strategy.getResultantText())
      }
      out.flush()
      out.close()
      reader.close()
      return baos.toByteArray()
    }

    fun parsePdfFile(pdf: String, txt: String){
      val filePdf = Files.readAllBytes(File(pdf).toPath())
      val out = parsePdf(filePdf)
      FileOutputStream(txt).use { fos -> fos.write(out) }
    }

    fun parsePdfFile2(pdf: String, txt: String){
      val doc = PDDocument.load(File(pdf))
      val text: String = PDFTextStripper().getText(doc)
      FileOutputStream(txt).use { fos -> fos.write(text.toByteArray()) }
    }
  */
  fun parsePdfFile3(pdf: String, txt: String) {
    val pdfParser = PDFParser(RandomAccessFile(File(pdf), "r"))
    pdfParser.parse()
    val pdDocument = PDDocument(pdfParser.document)
    val pdfTextStripper = PDFLayoutTextStripper()
    val text = pdfTextStripper.getText(pdDocument)
    FileOutputStream(txt).use { fos -> fos.write(text.toByteArray()) }
  }
}

fun main() {
  ExportTxt.parsePdfFile3("/tmp/pdf/pedido.pdf", "/tmp/pdf/pedido.txt")
}