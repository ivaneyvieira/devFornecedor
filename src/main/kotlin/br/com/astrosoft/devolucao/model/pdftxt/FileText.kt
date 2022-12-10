package br.com.astrosoft.devolucao.model.pdftxt

import java.io.File

class FileText {
  private val lines = mutableListOf<Line>()

  fun addLine(line: Line) {
    lines.add(line)
  }

  fun getLine(num : Int): Line? {
    return lines.firstOrNull {
      it.num == num
    }
  }

  fun findLine(text: String): List<Line> {
    return lines.filter { line ->
      line.find(text)
    }
  }

  fun localizaColunas(): Line? {
    val max = lines.maxOf { it.countTitleWord }
    return lines.firstOrNull { it.countTitleWord == max }
  }

  fun lines(start: Int): List<Line> {
    return lines.subList(start - 1, lines.size).toList()
  }

  fun listLinesDados(): DataLine? {
    val lineTitle = localizaColunas() ?: return null
    val colunas = lineTitle.columns()
    return DataLine(colunas)
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
  if (lineColumn != null) teste01(lineColumn)
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

