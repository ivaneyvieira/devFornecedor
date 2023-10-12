package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NotaXML
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

class PlanilhaNotaXml {
  private val campos: List<Campo<*, NotaXML>> = listOf(
    CampoInt("Lj") { lj },
    CampoInt("NI") { ni },
    CampoString("Emissão") { dataEmissao.format() },
    CampoString("Entrada") { data.format() },
    CampoString("Nota") { nfe },
    CampoString("For NF") { fornNota },
    CampoString("Referência") { refPrdx },
    CampoString("EAN") { barcodex },
    CampoString("Cód") { codigo },
    CampoString("Descrição") { descricaox },
    CampoString("Unid") { unidadex },
    CampoNumber("Qtd XML") { quant },
    CampoString("Ud Saci") { unidadeSaci },
    CampoInt("Qtd Saci") { quantSaci },
    CampoString("CFOP") { cfopx },
    CampoString("CST") { cstx },
    CampoNumber("ICMS") { alIcmsx },
    CampoNumber("IPI") { alIpix },
    CampoNumber("MVA") { mvax },
    CampoNumber("PIS") { alPisx },
    CampoNumber("COFINS") { alCofinsx },
  )

  fun grava(listaNotas: List<NotaXML>): ByteArray {
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

  fun <T> PSheet.row(campos: List<Campo<*, T>>, bean: T) {
    val row = this.createRow(this.physicalNumberOfRows)
    val creationHelper = workbook.creationHelper

    campos.forEachIndexed { index, campo ->
      val cellValue = campo.produceValue(bean)
      autoSizeColumn(index)
      row.createCell(index).apply {
        when (cellValue) {
          is String    -> setCellValue(cellValue)
          is Int       -> {
            val style = workbook.createCellStyle()
            style.dataFormat = creationHelper.createDataFormat().getFormat("0")
            cellStyle = style
            setCellValue(cellValue.toDouble())
          }

          is Number    -> {
            val style = workbook.createCellStyle()
            style.dataFormat = creationHelper.createDataFormat().getFormat("0.00")
            cellStyle = style
            setCellValue(cellValue.toDouble())
          }

          is LocalDate -> setCellValue(cellValue.format())
          else         -> setCellValue(cellValue.toString())
        }
      }
    }
  }
}