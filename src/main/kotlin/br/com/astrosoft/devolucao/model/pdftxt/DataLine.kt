package br.com.astrosoft.devolucao.model.pdftxt

data class DataLine(val column: List<Column>) {
  fun find(ref: String): Line? {
    val listLineDados = column.map { it.lineDados() }
    val ret = listLineDados.firstNotNullOfOrNull { lines ->
      lines.firstOrNull { line ->
        line.find(ref)
      }
    }
    return ret
  }
}