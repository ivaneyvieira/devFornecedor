package br.com.astrosoft.devolucao.model.pdftxt

import br.com.astrosoft.framework.util.rpad
import br.com.astrosoft.framework.util.unaccent
import java.text.DecimalFormat

private val titleWord = listOf("Cod", "Codigo", "Descricao", "Qtde", "Un", "Item", "Produto", "Qtd", "Preco", "Total")

data class Line(val num: Int, val lineStr: String, val fileText: FileText, val double: Boolean = false) {
  private val form1 = DecimalFormat("#,##0.#####")
  private val form2 = DecimalFormat("0.#####")
  fun find(text: String?): Boolean {
    val textUnaccent = text?.unaccent() ?: return false
    return " $lineStr ".unaccent().contains(" $textUnaccent ")
  }

  fun item() = lineStr.trim().split("\\s+".toRegex()).getOrNull(0) ?: ""

  private fun findListVal(num: Number?, zeroPad: Int): List<String> {
    num ?: return emptyList()
    val zeros = if (zeroPad == 0) "" else "".rpad(zeroPad, "0")
    val strNum1 = form1.format(num)
    val strNum2 = form2.format(num)
    val virgulaZero1 = if (strNum1.contains(",")) zeros else ",$zeros"
    val virgulaZero2 = if (strNum2.contains(",")) zeros else ",$zeros"
    val val1 = strNum1 + virgulaZero1
    val val2 = strNum2 + virgulaZero2
    val val3 = val2.replace(",", ".")
    return listOf(val1, val2, val3).distinct()
  }

  fun find(num: Number?): Boolean {
    val list =
      findListVal(num, 0) + findListVal(num, 1) + findListVal(num, 2) + findListVal(num, 3) + findListVal(num, 4)
    return list.any { find(it) }
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
}