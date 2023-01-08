package br.com.astrosoft.devolucao.model.pdftxt

import br.com.astrosoft.framework.util.unaccent
import java.io.File

class FileText {
  private val lines = mutableListOf<Line>()

  fun clear() {
    lines.clear()
  }

  fun loadPDF(bytes: ByteArray) {
    val bytesTXT = ExportTxt.toTxt(bytes)
    loadTXT(bytesTXT)
  }

  fun loadTXT(bytes: ByteArray) {
    val text = String(bytes)
    val listLineStr = text.lines()
    clear()
    listLineStr.forEachIndexed { index, lineStr ->
      val pos = index + 1
      val line = Line(num = pos, lineStr = lineStr, fileText = this)
      addLine(line)
    }
  }

  fun addLine(line: Line) {
    lines.add(line)
  }

  fun getLine(num: Int): Line? {
    return lines.firstOrNull {
      it.num == num
    }
  }

  fun findLine(text: String): List<Line> {
    return filterDataLines().filter { line ->
      line.findRef(text)
    }
  }

  private fun filterDataLines(): List<Line> {
    return lines
  }

  fun localizaColunas(): List<Line> {
    val max = lines.maxOf { it.countTitleWord() }
    return lines.filter { it.countTitleWord() == max }
  }

  fun findColQuant(): List<LinePosition> {
    val lines = localizaColunas()
    return lines.mapNotNull { line ->
      val tokens = line.listPosTokens(Line.split2).toList()
      val token = titleQuant.firstNotNullOfOrNull { title ->
        tokens.firstOrNull { token ->
          token.text.unaccent().contains(title)
        }
      }

      if (token == null) null
      else LinePosition(token.start, "${token.text}      ")
    }
  }

  fun findColValor(): List<LinePosition> {
    val lines = localizaColunas()
    return lines.mapNotNull { line ->
      val tokens = line.listPosTokens(Line.split2).toList()
      val token = titleValor.firstNotNullOfOrNull { title ->
        tokens.firstOrNull { token ->
          token.text.unaccent().contains(title)
        }
      }

      if (token == null) null
      else LinePosition(token.start, "${token.text}      ")
    }
  }

  fun lines(start: Int): List<Line> {
    return lines.subList(start - 1, lines.size).toList()
  }

  fun isNotEmpty(): Boolean {
    return lines.isNotEmpty()
  }

  companion object {
    fun fromFile(filename: String): FileText {
      val fileText = FileText()
      val listLineStr = File(filename).readLines()
      listLineStr.forEachIndexed { index, lineStr ->
        val pos = index + 1
        val line = Line(num = pos, lineStr = lineStr, fileText = fileText)
        fileText.addLine(line)
      }
      return fileText
    }

    fun fromFile(bytes: ByteArray): FileText {
      val text = String(bytes)
      val fileText = FileText()
      val listLineStr = text.lines()
      listLineStr.forEachIndexed { index, lineStr ->
        val pos = index + 1
        val line = Line(num = pos, lineStr = lineStr, fileText = fileText)
        fileText.addLine(line)
      }
      return fileText
    }
  }
}

fun main() {
  val filename = "/home/ivaneyvieira/git/devFornecedor/pedidosPDF/pedido01.txt"
  val file = FileText.fromFile(filename)
  val lineColumn = file.localizaColunas()
}

private fun teste01(lineColumn: Line) {
  println(lineColumn.tituloColuna())
  val colunas = lineColumn.columns()
  println("'${lineColumn.lineStr}")
  colunas.forEach { col ->
    println("****************************************************")
    println("${col.title}: '${col.titleArea}' ${col.start} ${col.end}")
    col.dados().forEach {
      println(it)
    }
  }

  println("****************************************************")

  val col1 = colunas[0]
  col1.lineDados().forEach {
    println(it)
  }
}

