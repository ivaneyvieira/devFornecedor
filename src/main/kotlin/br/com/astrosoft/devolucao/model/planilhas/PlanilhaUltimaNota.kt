package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import br.com.astrosoft.framework.util.format
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaUltimaNota {
  private val campos: List<Campo<*, UltimaNotaEntrada>> = listOf(
    CampoInt("lj") { lj },
    CampoInt("ni") { ni },
    CampoString("data") { data.format() },
    CampoString("nfe") { nfe },
    CampoString("forn") { forn },
    CampoString("prod") { prod },
    CampoString("descricao") { descricao },
    CampoNumber("icmsn") { icmsn },
    CampoNumber("icmsp") { icmsp },
    CampoNumber("ipin") { ipin },
    CampoNumber("ipip") { ipip },
    CampoString("cstn") { cstn },
    CampoString("cstp") { cstp },
    CampoNumber("mvan") { mvan },
    CampoNumber("mvap") { mvap },
    CampoString("ncmn") { ncmn },
    CampoString("ncmp") { ncmp })

  fun grava(listaNotas: List<UltimaNotaEntrada>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stNotas = sheet("Notas SAP") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaNotas.forEach { nota ->
          val valores = campos.map { it.produceValue(nota) }
          row(valores, rowStyle)
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