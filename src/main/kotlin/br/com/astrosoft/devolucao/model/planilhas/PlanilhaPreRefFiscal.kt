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

class PlanilhaPreRefFiscal {
  private val campos: List<Campo<*, NfPrecEntrada>> =
      listOf(
        CampoInt("Lj") { ljCol },
        CampoInt("NI") { niCol },
        CampoString("Entrada") { data.format() },
        CampoString("Nota") { nfe },
        CampoString("F Cad") { fornCad },
        CampoString("F Nota") { fornNota },
        CampoString("Prod") { prod },
        CampoString("Descrição") { descricao },
        CampoString("Grade") { grade },
        CampoString("Un X") { unidadex },
        CampoNumber("Qtd X") { quantx },
        CampoString("Un S") { unidade },
        CampoInt("Qtd S") { quant },
        CampoString("Ref X") { refPrdx },
        CampoString("Ref P") { refPrdn },
        CampoString("Cód Barras XML") { barcodex },
        CampoString("Cód Barras Gtin") { barcodep },
        CampoString("Cód Barras Cad") { barcodec },
        CampoString("NCM X") { ncmx },
        CampoString("NCM P") { ncmp },
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

