package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
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

class PlanilhaNotasSap {
  private val campos: List<Campo<*, NotaDevolucaoSap>> =
      listOf(
          CampoInt("Código") { codigoFor },
          CampoString("Nome Fornecedor") { nomeFor },
          CampoInt("Código Saci") { vendno },
          CampoInt("Código Cliente") { custno },
          CampoInt("Loja") { storeno },
          CampoString("Nota Sap") { numero },
          CampoString("Nota Saci") { nfSaci },
          CampoString("Data de Lancamento") { dataLancamento.format() },
          CampoString("Data de Vencimento") { dataVencimento.format() },
          CampoNumber("Saldo") { saldo },
      )

  fun grava(listaNotas: List<NotaDevolucaoSap>): ByteArray {
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
        listaNotas.sortedBy { it.codigoFor }.forEach { nota ->
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