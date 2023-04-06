package br.com.astrosoft.devolucao.model.pdftxt

import br.com.astrosoft.framework.util.unaccent
import kotlin.math.absoluteValue

val titleQuant = listOf("Quantidade", "Qnt", "Quant", "Qtd", "Qtde").map {
  it.unaccent()
}

val titleValor =
  listOf(
    "Preco Unit",
    "Un. Liq",
    "Valor Unit",
    "VR.UNIT",
    "Pr.Unit",
    "P.Unitario",
    "Preco",
    "Unitario",
    "Unit",
    "Liq"
  ).map {
    it.unaccent()
  }
private val titleWord = listOf("Cod", "Codigo", "Descricao", "Un", "Item", "Produto", "Total").map {
  it.unaccent()
} + titleQuant + titleValor

data class Line(val num: Int, val lineStr: String, val fileText: FileText, val double: Boolean = false) {
  private fun find(text: String?, sepSplit: Regex): Boolean {
    return findIndex(text, sepSplit).isNotEmpty()
  }

  fun listPosTokens(sepSplit: Regex): Sequence<LinePosition> {
    val seq = sequence {
      val lineStrUnacent = lineStr.unaccent()
      val tokens = lineStrUnacent.split(sepSplit)
      var index = 0
      tokens.filter { it.isNotBlank() }.forEach { token ->
        index = " $lineStrUnacent ".indexOf(" $token ", startIndex = index)
        val pos = createLinePosition(index, token)
        if (pos != null) yield(pos)
        index += token.length
      }
    }
    return seq
  }

  private fun findIndex(text: String?, sepSplit: Regex): List<LinePosition> {
    text ?: return emptyList()
    val list = listPosTokens(sepSplit).toList()
    val seq = list.filter {
      it.text.unaccent() == text.unaccent()
    }
    return seq
  }

  private fun createLinePosition(start: Int, text: String): LinePosition? {
    val startIndex = 0
    val endIndex = lineStr.length - 1
    return if (start in startIndex..endIndex) {
      LinePosition(start, text.trim())
    } else null
  }

  private fun midLine(index: Int): String {
    val startIndex = 0
    val endIndex = lineStr.length - 1
    return if (index in startIndex..endIndex) {
      lineStr.substring(index)
    } else ""
  }

  private fun midLine(start: Int?, end: Int?): String? {
    start ?: return null
    end ?: return null
    val startIndex = 0
    val endIndex = if (end > lineStr.length) lineStr.length else end
    return if (start in startIndex..endIndex) {
      lineStr.substring(start, end)
    } else ""
  }

  private fun item() = listPosTokens(split1).toList().getOrNull(0)?.text ?: ""

  fun findIndex(num: Number?): List<LinePosition> {
    num ?: return emptyList()
    val numStr = listPosTokens(split1).mapNotNull { pos ->
      val str = pos.text
      val number = strToNumber(str)
      if (number == null) null
      else {
        val dif = (num.toDouble() - number.toDouble()).absoluteValue
        if (dif <= 0.05) str
        else null
      }
    }.distinct()
    return numStr.flatMap { str -> findIndex(str, split1) }.toList()
  }

  fun countTitleWord(): Int {
    val count = titleWord.count { find(it, split1) }
    return count
  }

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

  fun tituloColuna(): Sequence<String> {
    return listPosTokens(split2).map { it.text }
  }

  fun columns(): List<Column> {
    val titles = this.tituloColuna().toList()
    var posIndex = 0
    val sequence = sequence<Column> {
      titles.forEachIndexed { index, title ->
        val isFirst = index == 0
        val isLast = index == (titles.toList().size - 1)
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

  private fun strToNumber(strNum: String?): Number? {
    strNum ?: return null
    val str = strNum.replace("[^0-9,.]+".toRegex(), " ").trim()
    val regNumPoint = "\\.[0-9]{3},".toRegex()
    val num01 = if (regNumPoint.containsMatchIn(str)) str.replace(".", "") else str
    return num01.trim().replace(',', '.').toDoubleOrNull()
  }

  private fun String?.decompose(): String? {
    this ?: return ""
    return if (this.contains("/")) this.split("/").getOrNull(1)?.trim() else this.trim()
  }

  fun getInt(start: Int?, end: Int?): Int? {
    start ?: return null
    end ?: return null
    val midLine = midLine(start = posStart(start), end = posEnd(end))?.trim()
    val decomposeInt = midLine.decompose()
    val strInt = decomposeInt?.split(split1)?.getOrNull(0)
    val int = strToNumber(strInt)?.toInt()
    return int
  }

  fun getDouble(start: Int?, end: Int?): Double? {
    start ?: return null
    end ?: return null
    val strDouble = midLine(start = posStart(start), end = posEnd(end))?.trim()?.split(split1)?.getOrNull(0)
    val double = strToNumber(strDouble)?.toDouble()
    return double
  }

  fun findRef(ref: String?): Boolean {
    return find(ref, split1)
  }

  fun findInDescricao(ref: String?): Boolean {
    return lineStr.contains("[$ref]")
  }

  private fun midChar(index: Int): Char? {
    return lineStr.getOrNull(index)
  }

  private fun posEnd(end: Int): Int? {
    return if (midChar(end) == ' ') end
    else {
      val token = listPosTokens(split1)
      token.map { it.end }.filter { it >= end }.minOrNull()
    }
  }

  private fun posStart(start: Int): Int? {
    return if (midChar(start) == ' ') start
    else {
      val token = listPosTokens(split1)
      token.map { it.start }.filter { it <= start }.maxOrNull()
    }
  }

  fun isNotEmpty(): Boolean {
    return lineStr.trim().isNotEmpty()
  }

  companion object {
    val split1 = "\\s+".toRegex()
    val split2 = "\\s\\s+".toRegex()
  }
}

data class LinePosition(val start: Int, val text: String) {
  val end
    get() = start + text.length
}