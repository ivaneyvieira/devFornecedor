package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
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

class PlanilhaFretePerDif {
  private val campos: List<Campo<*, NfPrecEntrada>> =
      listOf(
        CampoInt("lj") { lj },
        CampoInt("ni") { ni },
        CampoString("data") { data.format() },
        CampoString("nfe") { nfe },
        CampoString("forn Cad") { fornCad },
        CampoString("forn Nota") { fornNota },
        CampoString("prod") { prod },
        CampoString("descrição") { descricao },
        CampoString("grade") { grade },
        CampoNumber("P NF") { pesoBrutoTotal ?: 0.00 },
        CampoNumber("P Prd") { pesoBrutoPrd ?: 0.00 },
        CampoNumber("Qtd") { (quant ?: 0) * 1.00 },
        CampoNumber("P Bruto") { pesoBruto ?: 0.0 },
        CampoNumber("R$ NF") { preconTotal ?: 0.00 },
        CampoNumber("$ F Kg") { freteKg ?: 0.00 },
        CampoNumber("R$ Frete") { freteTotal ?: 0.00 },
        CampoNumber("% F NF") { fretePerNf ?: 0.00 },
        CampoNumber("% F Prc") { fretePerPrc ?: 0.00 },
      )

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
