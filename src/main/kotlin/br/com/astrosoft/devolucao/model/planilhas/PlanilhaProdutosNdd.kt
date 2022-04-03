package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.ProdutoNotaEntradaNdd
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import java.io.ByteArrayOutputStream

class PlanilhaProdutosNdd {
  private val campos: List<Campo<*, ProdutoNotaEntradaNdd>> =
          listOf(
            CampoInt("Código") { codigo.toIntOrNull() ?: 0 },
            CampoString("Código de Barras") { codBarra },
            CampoString("Descrição") { descricao },
            CampoString("NCM") { ncm },
            CampoString("CST") { cst },
            CampoString("CFOP") { cfop },
            CampoString("UN") { un },
            CampoInt("Quantidade") { quantidade.toInt() },
            CampoNumber("Valor Unitário") { valorUnitario },
            CampoNumber("Valor Total") { valorTotal },
            CampoNumber("B. Calc ICMS") { baseICMS },
            CampoNumber("Valor IPI") { valorIPI },
            CampoNumber("Alíq IPI") { aliqIPI },
            CampoNumber("Alíq ICMS") { aliqICMS },
                )

  fun grava(listaNotas: List<ProdutoNotaEntradaNdd>, titulo: String): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stNotas = sheet("Produtos NDD") {
        this.addMergedRegion(CellRangeAddress.valueOf("A1:N1"))
        row(listOf(titulo), headerStyle)
        row(listOf())
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