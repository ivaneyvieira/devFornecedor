package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import br.com.astrosoft.framework.util.format
import com.github.nwillc.poink.PSheet
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream
import java.time.LocalDate

class PlanilhaNfPrec(val fiscal: Boolean) {
  private val campos: List<Campo<*, NfPrecEntrada>> =
    if (fiscal) listOf(
      CampoInt("lj") { lj },
      CampoInt("ni") { ni },
      CampoString("data") { data.format() },
      CampoString("nfe") { nfe },
      CampoString("forn Cad") { fornCad },
      CampoString("forn Nota") { fornNota },
      CampoString("prod") { prod },
      CampoString("descrição") { descricao },
      CampoInt("quant") { quant ?: 0 },
      CampoNumber("freten") { freten },
      CampoNumber("fretep") { fretep },
      CampoNumber("icmsn") { icmsn },
      CampoNumber("icmsp") { icmsp },
      CampoNumber("ipin") { ipin },
      CampoNumber("ipip") { ipip },
      CampoString("cstn") { cstn },
      CampoString("cstp") { cstp },
      CampoNumber("mvan") { mvanApŕox },
      CampoNumber("mvap") { mvap },
    )
    else listOf(CampoInt("lj") { lj },
      CampoInt("ni") { ni },
      CampoString("data") { data.format() },
      CampoString("nfe") { nfe },
      CampoString("forn Cad") { fornCad },
      CampoString("forn Nota") { fornNota },
      CampoString("prod") { prod },
      CampoString("descrição") { descricao },
      CampoString("grade") { grade },
      CampoString("refn") { refPrdn },
      CampoString("refp") { refPrdp },
      CampoString("barrasn") { barcoden },
      CampoString("barrasp") { barcodep },
      CampoString("ncmn") { ncmn },
      CampoString("ncmp") { ncmp })

  fun grava(listaNotas: List<NfPrecEntrada>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }

      val stNotas = sheet("Notas SAP") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaNotas.forEach { nota ->
          row(campos, nota)
        }
      }

      campos.forEachIndexed { index, _ ->
        stNotas.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}

fun <T> PSheet.row(campos: List<Campo<*, T>>, bean: T) {
  val row = this.createRow(this.physicalNumberOfRows)
  val creationHelper = workbook.creationHelper

  campos.forEachIndexed { index, campo ->
    val cellValue = campo.produceValue(bean)
    autoSizeColumn(index)
    row.createCell(index).apply {
      when (cellValue) {
        is String -> setCellValue(cellValue)
        is Int -> {
          val style = workbook.createCellStyle()
          style.dataFormat = creationHelper.createDataFormat().getFormat("0")
          cellStyle = style
          setCellValue(cellValue.toDouble())
        }

        is Number -> {
          val style = workbook.createCellStyle()
          style.dataFormat = creationHelper.createDataFormat().getFormat("0.00")
          cellStyle = style
          setCellValue(cellValue.toDouble())
        }

        is LocalDate -> setCellValue(cellValue.format())
        else -> setCellValue(cellValue.toString())
      }
    }
  }
}