package br.com.astrosoft.devolucao.model.pdftxt

data class DataLine(val column: List<Column>, val lines: List<Line>) {
  fun find(ref: String): Line? {
    return lines.firstOrNull { line ->
      line.find(ref)
    }
  }
}