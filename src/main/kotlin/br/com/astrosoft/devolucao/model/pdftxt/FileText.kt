package br.com.astrosoft.devolucao.model.pdftxt

import br.com.astrosoft.framework.util.unaccent
import java.io.File

class FileText {
  private val lines = mutableListOf<Line>()

  fun clear() {
    lines.clear()
  }

  fun loadPDF(bytes: ByteArray) {
    val listLineStr4 = toStringList(bytes, 4)
    val listLineStr3 = toStringList(bytes, 3)
    loadLines(listLineStr4 + listLineStr3)
  }

  private fun toStringList(bytes: ByteArray, fidexChar: Int): List<String> {
    val bytesTXT = ExportTxt.toTxt(bytes, fidexChar)
    val text = String(bytesTXT)
    return text.lines()
  }

  private fun loadLines(listLineStr: List<String>) {
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
    val dataLines = filterDataLines()
    val lineFound01 = dataLines.filter { line ->
      line.findRef(text)
    }

    val lineFound = lineFound01.ifEmpty {
      val lineFound01 = dataLines.filter { line ->
        val result = line.findInDescricao(text)

        if (result) true
        else {
          val indexOfLine = dataLines.indexOf(line)
          val lineNext = dataLines.getOrNull(indexOfLine + 1)
          val strNext = lineNext?.lineStr ?: ""
          val str = line.lineStr
          if (strNext.length < (str.length / 4)) {
            lineNext?.findInDescricao(text) == true
          }
          else false
        }
      }
      lineFound01
    }

    return lineFound
  }

  private fun filterDataLines(): List<Line> {
    return lines.filter { it.isNotEmpty() }
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

