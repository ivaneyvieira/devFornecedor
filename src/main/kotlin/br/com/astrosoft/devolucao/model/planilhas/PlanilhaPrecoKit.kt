package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NfPrecEntradaKit
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaPrecoKit {
  private val campos: List<Campo<*, NfPrecEntradaKit>> =
      listOf(
        CampoString("Código") { prdnoKit.trim() },
        CampoString("Descrição") { descricaoKit },
        CampoInt("Quant") { quantKit },
        CampoNumber("V. Unit") { valorUnitarioKit },
        CampoNumber("V. Total") { valorTotalKit },
        CampoInt("Código") { codigo },
        CampoString("Descrição") { descricao },
        CampoInt("Quant") { quant },
        CampoNumber("V. Unit") { valorUnitario },
        CampoNumber("V. Total") { valorTotal },
      )

  fun grava(listaNotas: List<NfPrecEntradaKit>): ByteArray {
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
