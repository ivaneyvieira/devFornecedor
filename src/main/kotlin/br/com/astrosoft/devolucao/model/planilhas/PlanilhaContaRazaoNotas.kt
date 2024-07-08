package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.ContaRazaoNota
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
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

class PlanilhaContaRazaoNotas {
  private val campos: List<Campo<*, ContaRazaoNota>> =
      listOf(
        CampoInt("Loja") { loja },
        CampoInt("NI") { ni },
        CampoString("NF") { nf },
        CampoString("Emissão") { emissao.format() },
        CampoString("Entrada") { entrada.format() },
        CampoString("Vencimento") { vencimento.format() },
        CampoNumber("Valor Nota") { valorNota },
        CampoInt("Fornecedor") { vendno },
        CampoString("Fornecedor Nome") { fornecedor },
        CampoString("Obs") { obs },
        CampoString("Situacao") { situacao },
        CampoString("Obs Parcela") { obsParcela },
      )

  fun grava(listaNotas: List<ContaRazaoNota>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stNotas = sheet("Produtos") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaNotas.sortedBy { it.loja }.forEach { produto ->
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