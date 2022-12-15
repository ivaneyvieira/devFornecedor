package br.com.astrosoft.devolucao.model.pdftxt

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.unaccent
import java.text.DecimalFormat

private val titleWord = listOf("Cod", "Codigo", "Descricao", "Qtde", "Un", "Item", "Produto", "Qtd", "Preco", "Total")


data class Line(val num: Int, val lineStr: String, val fileText: FileText, val double : Boolean = false) {
  private val form1 = DecimalFormat("#,##0.#####")
  private val form2 = DecimalFormat("0.#####")
  fun find(text: String?): Boolean {
    val textUnaccent = text?.unaccent() ?: return false
    return " $lineStr ".unaccent().contains(" $textUnaccent ")
  }

  fun item() = lineStr.trim().split("\\s+".toRegex()).getOrNull(0) ?: ""

  fun find(num: Number?): Boolean {
    num ?: return false
    val val1 = form1.format(num)
    val val2 = form2.format(num)
    return find(val1) || find(val2)
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