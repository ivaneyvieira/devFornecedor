package br.com.astrosoft.devolucao.model.pdftxt

import br.com.astrosoft.framework.util.rpad
import br.com.astrosoft.framework.util.unaccent
import java.text.DecimalFormat
import kotlin.math.absoluteValue

private val titleWord = listOf("Cod", "Codigo", "Descricao", "Qtde", "Un", "Item", "Produto", "Qtd", "Preco", "Total")

data class Line(val num: Int, val lineStr: String, val fileText: FileText, val double: Boolean = false) {
  private val form1 = DecimalFormat("#,##0.#####")
  private val form2 = DecimalFormat("0.#####")

  fun find(text: String?): Boolean {
    return findIndex(text) != null
  }

  fun findIndex(text: String?): LinePosition? {
    val textUnaccent = text?.unaccent() ?: return null
    val lineStrSpace = " $lineStr "
    val lineStrNoraml = lineStrSpace.unaccent()
    val index = lineStrNoraml.indexOf(" $textUnaccent ")
    val textLine = midLine(index).split(" ").getOrNull(0) ?: return null
    return createLinePosition(start = index, text = textLine)
  }

  private fun createLinePosition(start: Int, text: String): LinePosition? {
    val startIndex = 0
    val endIndex = lineStr.length - 1
    return if (start in startIndex..endIndex) {
      LinePosition(start, text)
    }
    else null
  }

  private fun midLine(index: Int): String {
    val startIndex = 0
    val endIndex = lineStr.length - 1
    return if (index in startIndex..endIndex) {
      lineStr.substring(index)
    }
    else ""
  }

  private fun midLine(start: Int, end: Int): String {
    val startIndex = 0
    val endIndex = if (end > lineStr.length) lineStr.length else end
    return if (start in startIndex..endIndex) {
      lineStr.substring(start, end)
    }
    else ""
  }

  fun item() = lineStr.trim().split("\\s+".toRegex()).getOrNull(0) ?: ""

  private fun findListVal(num: Number?, zeroPad: Int): List<String> {
    num ?: return emptyList()
    val zeros = if (zeroPad == 0) "" else "".rpad(zeroPad, "0")
    val strNum1 = form1.format(num)
    val strNum2 = form2.format(num)
    val virgulaZero1 = if (zeroPad == 0) "" else if (strNum1.contains(",")) zeros else ",$zeros"
    val virgulaZero2 = if (zeroPad == 0) "" else if (strNum2.contains(",")) zeros else ",$zeros"
    val val1 = strNum1 + virgulaZero1
    val val2 = strNum2 + virgulaZero2
    val val3 = val2.replace(",", ".")
    return listOf(val1, val2, val3).distinct()
  }

  fun find(num: Number?): Boolean {
    return findIndex(num) != null
  }

  fun findIndex(num: Number?): LinePosition? {
    num ?: return null
    val numStr = lineStr.split(" +".toRegex()).mapNotNull { str ->
      val number = strToNumber(str)
      if (number == null) null
      else {
        val dif = (num.toDouble() - number.toDouble()).absoluteValue
        if(dif < 0.5)
           str
        else null
      }
    }
    return findIndex(numStr.firstOrNull())
  }

  val countTitleWord = titleWord.count { find(it) }

  fun mid(start: Int, end: Int): String {
    return if (lineStr == "") ""
    else {
      val pstart = if (start < 1) 1 else start
      val pend = if (end > lineStr.length) lineStr.length else end
      if (pstart > lineStr.length) ""
      else if (pend < 1) ""
      else lineStr.substring(startIndex = pstart - 1, endIndex = pend)
    }
  }

  fun mid(start: Int): String = mid(start, start)

  fun tituloColuna(): List<String> {
    val lineStr = this.lineStr
    return lineStr.split("  +".toRegex()).map { it.trim() }
  }

  fun columns(): List<Column> {
    val titles = this.tituloColuna()
    var posIndex = 0
    val sequence = sequence<Column> {
      titles.forEachIndexed { index, title ->
        val isFirst = index == 0
        val isLast = index == (titles.size - 1)
        val posStart = if (isFirst) 0
        else {
          val prevTitle = titles[index - 1]
          lineStr.indexOf(string = prevTitle, startIndex = posIndex) + prevTitle.length + 1
        }
        val posEnd = if (isLast) lineStr.length
        else {
          val nextTitle = titles[index + 1]
          lineStr.indexOf(string = nextTitle, startIndex = posStart + title.length - 1)
        }
        val col = Column(line = this@Line, numCol = index + 1, title = title, start = posStart, end = posEnd)
        yield(col)
        posIndex = posStart
      }
    }
    return sequence.toList()
  }

  private fun strToNumber(str: String): Number? {
    return str.trim().replace(".", "").replace(',', '.').toDoubleOrNull()
  }

  fun getInt(start: Int?, end: Int?): Int? {
    start ?: return null
    end ?: return null
    val strInt = midLine(start = posStart(start), end = posEnd(end))
    val int = strInt.trim().replace(".", "").replace(',', '.').toDoubleOrNull()?.toInt()
    return int
  }

  fun getDouble(start: Int?, end: Int?): Double? {
    start ?: return null
    end ?: return null
    val strDouble = midLine(start = posStart(start), end = posEnd(end))
    val double = strDouble.trim().replace(".", "").replace(',', '.').toDoubleOrNull()
    return double
  }

  private fun midChar(index: Int): Char? {
    return lineStr.getOrNull(index)
  }

  private fun posEnd(end: Int): Int {
    val char = midChar(end) ?: return lineStr.length
    return if (char == ' ') end
    else {
      for (p in end..lineStr.length) {
        val next = midChar(p + 1)
        if (next == null || next == ' ') return p
      }
      return lineStr.length
    }
  }

  private fun posStart(start: Int): Int {
    val char = midChar(start) ?: return 0
    return if (char == ' ') start
    else {
      for (p in start downTo 0) {
        val prev = midChar(p - 1)
        if (prev == null || prev == ' ') return p
      }
      return lineStr.length
    }
  }
}

data class LinePosition(val start: Int, val text: String) {
  val end
    get() = start + text.length
}