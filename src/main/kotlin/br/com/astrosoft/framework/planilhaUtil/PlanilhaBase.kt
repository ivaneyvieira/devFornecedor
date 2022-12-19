package br.com.astrosoft.framework.planilhaUtil

import com.github.nwillc.poink.workbook
import io.github.rushuat.ocell.document.Document
import io.github.rushuat.ocell.document.DocumentOOXML
import java.time.LocalTime
import kotlin.reflect.full.createInstance

abstract class PlanilhaBase<T : IDadosPlanilha>(private val list: List<T> = emptyList()) {
  private val listaPlanilha = mutableListOf<T>()

  init {
    readCollection(list)
  }

  abstract fun toBean(dataRow: DataRow): T

  private fun readXlsx(filename: String, tipo: ETipoFindCol): List<DataRow> {
    var sequence: Sequence<DataRow>? = null
    workbook(filename) {
      sheet(0) {
        sequence = sequence {
          val colunas = mutableListOf<String>()
          var rowIndex = 0
          this@sheet.rowIterator().forEach { row ->
            if (rowIndex == 0) {
              row.iterator().forEach { cell ->
                colunas.add(cell.stringCellValue)
              }
            }
            else {
              val map = DataRow(tipo)
              var colIndex = 0
              colunas.forEachIndexed { index, col ->
                val value = row.getCell(index)?.toString() ?: ""
                map[col] = value
                colIndex++
              }
              if (!map.isEmpty) yield(map)
            }
            rowIndex++
          }
        }
      }
    }
    return sequence.orEmpty().toList()
  }

  fun readExcel(filename: String, tipo: ETipoFindCol) {
    val dataFrame = readXlsx(filename, tipo)
    val lista = dataFrame.map {
      val bean = toBean(it)
      bean.update()
      bean
    }
    readCollection(lista)
  }

  abstract fun updateIndex(lista: Collection<T>)

  open fun readCollection(lista: Collection<T>) {
    listaPlanilha.clear()
    listaPlanilha.addAll(lista)
    updateIndex(lista)
  }

  fun listaPlanilha(): List<T> = listaPlanilha

  fun salvaPlanilha(filename: String) {
    println(LocalTime.now())
    println("Gravando o aquivo $filename com ${listaPlanilha.size} linhas")
    DocumentOOXML().use { doc ->
      doc.addSheet(listaPlanilha)
      doc.toFile(filename)
    }
    println(LocalTime.now())
  }

  val size
    get() = listaPlanilha.size
}

inline fun <T, R : Comparable<R>, reified B : PlanilhaBase<T>> B.sortedBy(crossinline selector: (T) -> R?): B {
  val lista = listaPlanilha().sortedBy(selector)
  val planilhaBase = B::class.createInstance()
  planilhaBase.readCollection(lista)
  return planilhaBase
}

inline fun <T, reified B : PlanilhaBase<T>> B.filter(predicate: (T) -> Boolean): B {
  val lista = listaPlanilha().filter(predicate)
  val planilhaBase = B::class.createInstance()
  planilhaBase.readCollection(lista)
  return planilhaBase
}

inline fun <T, K, reified B : PlanilhaBase<T>> B.groupBy(keySelector: (T) -> K): Map<K, B> {
  val maps = listaPlanilha().groupBy(keySelector)

  return maps.mapValues { ent ->
    val lista = ent.value
    val planilhaBase = B::class.createInstance()
    planilhaBase.readCollection(lista)
    planilhaBase
  }
}