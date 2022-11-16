package br.com.astrosoft.framework.view.export

import com.github.mvysny.kaributools.fetchAll
import com.github.mvysny.kaributools.header2
import com.github.nwillc.poink.PSheet
import com.github.nwillc.poink.PWorkbook
import com.github.nwillc.poink.workbook
import com.vaadin.flow.component.grid.Grid
import org.apache.poi.ss.usermodel.*
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ExcelExporter<T : Any>(val grid: Grid<T>) {
  private var dateFormat: Short = 0
  private var timeStampFormat: Short = 0
  private var calendarFormat: Short = 0
  private var integerFormat: Short = 0
  private var numberFormat: Short = 0
  val beanType = grid.beanType

  //fun exporter(title: String): PWorkbook = exporter(title, fetchAll())

  private fun exporter(title: String, dataGrid: List<T>): PWorkbook {
    val columns = columns()
    return workbook {
      val fontNegrito = fontNegrito()
      val fontNormal = fontNormal()

      val format = this.createDataFormat()
      dateFormat = format.getFormat("DD/MM/YYYY")
      timeStampFormat = format.getFormat("DD/MM/YYYY HH:MM:SS")
      calendarFormat = format.getFormat("MMM D, YYYY")
      integerFormat = format.getFormat("0")
      numberFormat = format.getFormat("#,##0.00")

      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        setFont(fontNegrito)
      }
      val bodyStyle = cellStyle("Body") {
        fillForegroundColor = IndexedColors.BLACK.index
        fillPattern = FillPatternType.NO_FILL
        setFont(fontNormal)
      }
      sheet(title) { // Add a row with a style
        row(columns.map { it.header2 }, headerStyle) // Add a row without style
        dataGrid.forEach { bean ->
          val values = columns.map { col ->
            val prop = property(col.key)
            prop?.get(bean)
          }
          rowFormat(values, bodyStyle)
        }
        columns.forEachIndexed { index, _ ->
          autoSizeColumn(index)
        }
      }
    }
  }

  private fun PWorkbook.fontNegrito(): Font? {
    val fontNegrito = createFont()
    fontNegrito.bold = true
    fontNegrito.fontHeightInPoints = 10
    fontNegrito.fontName = "Arial"
    return fontNegrito
  }

  private fun PWorkbook.fontNormal(): Font? {
    val fontNegrito = createFont()
    fontNegrito.bold = false
    fontNegrito.fontHeightInPoints = 10
    fontNegrito.fontName = "Arial"
    return fontNegrito
  }

  fun exporterToByte(title: String): ByteArray = exporterToByte(title, fetchAll())

  fun exporterToByte(title: String, itens: List<T>): ByteArray {
    val wb = exporter(title, itens)
    val output = ByteArrayOutputStream()
    wb.write(output)
    return output.toByteArray()
  }

  private fun fetchAll(): List<T> {
    return grid.dataProvider.fetchAll()
  }

  private fun columns(): List<Grid.Column<T>> {
    val props = beanType.kotlin.memberProperties.map { it.name }
    return grid.columns.filter { col: Grid.Column<T> ->
      col.key in props
    }
  }

  private fun property(name: String): KProperty1<T, *>? {
    return beanType.kotlin.memberProperties.firstOrNull { it.name == name }
  }

  fun PSheet.rowFormat(cells: List<Any?>, style: CellStyle? = null): List<Cell> {
    val cellList = mutableListOf<Cell>()
    val row = this.createRow(this.physicalNumberOfRows)
    var col = 0
    cells.forEach { cellValue ->
      cellList.add(row.createCell(col++).apply {
        cellStyle = style
        when (cellValue) {
          null             -> setCellValue("")
          is String        -> setCellValue(cellValue)
          is Int           -> {
            cellStyle = cloneAndFormat(style, integerFormat)
            setCellValue(cellValue.toDouble())
          }

          is Number        -> {
            cellStyle = cloneAndFormat(style, numberFormat)
            setCellValue(cellValue.toDouble())
          }

          is LocalDateTime -> {
            cellStyle = cloneAndFormat(style, timeStampFormat)
            setCellValue(cellValue)
          }

          is LocalDate     -> {
            cellStyle = cloneAndFormat(style, dateFormat)
            setCellValue(cellValue)
          }

          is Calendar      -> {
            cellStyle = cloneAndFormat(style, calendarFormat)
            setCellValue(cellValue)
          }

          else             -> setCellValue(cellValue.toString())
        }
      })
    }
    return cellList
  }

  private fun PSheet.cloneAndFormat(style: CellStyle?, format: Short) = workbook.createCellStyle().apply {
    if (style != null) {
      cloneStyleFrom(style)
    }
    dataFormat = format
  }
}