package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NfEntradaFrete
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

class PlanilhaCte {
  private val campos: List<Campo<*, NfEntradaFrete>> =
      listOf(
          CampoInt("Loja") { loja },
          CampoString("NI") { ni },
          CampoString("NF") { nf },
          CampoString("Emissão") { emissao.format() },
          CampoString("Entrada") { entrada.format() },
          CampoInt("For NF") { vendno },
          CampoNumber("R$ Prd") { totalPrd },
          CampoNumber("R$ NF") { valorNF },
          CampoInt("Transp") { carrno },
          CampoString("Nome") { carrName },
          CampoInt("CTe") { cte },
          CampoString("Emissão") { emissaoCte.format() },
          CampoString("Entrada") { entradaCte.format() },
          CampoNumber("R$ Frete Fat") { valorCte ?: 0.00 },
          CampoNumber("R$ Frete Cal") { totalFrete ?: 0.00 },
          CampoNumber("P Bruto") { pesoBruto ?: 0.00 },
          CampoNumber("Peso Cub") { pesoCub ?: 0.00 },
          CampoNumber("Cub") { cub ?: 0.00 },
          CampoNumber("R$ F Peso") { fretePeso ?: 0.00 },
          CampoNumber("R$ Adv") { adValore ?: 0.00 },
          CampoNumber("R$ Gris") { gris ?: 0.00 },
          CampoNumber("Taxa") { taxa ?: 0.00 },
          CampoNumber("Outros") { outro ?: 0.00 },
          CampoNumber("ICMS") { icms ?: 0.00 },
      )

  fun grava(listaNotas: List<NfEntradaFrete>): ByteArray {
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
