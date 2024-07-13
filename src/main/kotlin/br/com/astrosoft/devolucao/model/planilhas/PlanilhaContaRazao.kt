package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.ContaRazao
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaContaRazao {
  private val campos: List<Campo<*, ContaRazao>> =
      listOf(
        CampoString("Número") { numeroConta },
        CampoString("Descrição") { descricaoConta },
        CampoInt("Quantidade") { quantNotas },
        CampoNumber("Total") { valorTotal },
      )

  fun grava(listaNotas: List<ContaRazao>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stNotas = sheet("Conta Razão") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaNotas.forEach { produto ->
          val valores = campos.map { it.produceValue(produto) }
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