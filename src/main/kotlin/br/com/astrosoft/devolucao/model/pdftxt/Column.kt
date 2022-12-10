package br.com.astrosoft.devolucao.model.pdftxt

data class Column(val line: Line, val numCol: Int, val title: String, val start: Int, val end: Int) {
  val titleArea = line.mid(start, end)

  private fun dadosBruto() = line.fileText.lines(line.num + 1).map { line ->
    val inicio = line.mid(start - 2, start - 1).trim()
    val fim = line.mid(end + 1, end + 2).trim()
    val inicio2 = line.mid(start, start + 1).trim()
    val fim2 = line.mid(end - 1, end).trim()
    val dados = line.mid(start, end).trim()
    "${if (inicio == "" || inicio2 == "") "" else "..."}$dados${if (fim == "" || fim2 == "") "" else "..."}"
  }

  fun dados(): List<String> {
    val lineBruto = dadosBruto()
    return lineBruto.map { dadosBruto ->
      val split = dadosBruto.split("  +".toRegex())
      split.filter { item ->
        !(item.startsWith("...") || item.endsWith("..."))
      }.joinToString(" ")
    }
  }

  fun lineDados(): List<Line> {
    val dados = dados()
    val numLinhaDados = dados.mapIndexedNotNull { index, str ->
      if (str.isBlank()) null else index
    }
    val numColTitle = line.num

    return numLinhaDados.mapNotNull { index ->
      val nextDados = dados.getOrNull(index + 1)
      val double = nextDados?.trim() == ""
      val numCol = index + numColTitle + 1
      line.fileText.getLine(numCol)?.copy(double = double)
    }
  }
}