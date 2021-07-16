package br.com.astrosoft.devolucao.model.planilhas

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

class PlanilhaNotasPedidos {
  private val campos: List<Campo<*, ProdutosNotaSaida>> =
          listOf(
            CampoString("Rótulo") { rotulo },
            CampoInt("Fornecedor") { vendno },
            CampoInt("NI") { invno },
            CampoString("Emissão") { dateInvStr },
            CampoString("NF") { notaInv },
            CampoString("Ref do Fab") { refFor },
            CampoString("Código") { codigo },
            CampoString("Descrição") { descricao },
            CampoString("NCM") { ncm },
            CampoString("CST") { cst },
            CampoString("CFOP") { cfop },
            CampoString("Unid") { un },
            CampoInt("Quant") { qtde },
            CampoNumber("V. Unit") { valorUnitario },
            CampoNumber("V. Total") { valorTotalIpi },
            CampoNumber("Valor ST") { vst },
            CampoNumber("B. Cálc. ICMS") { baseICMS },
            CampoNumber("Valor ICMS") { valorICMS },
            CampoNumber("Valor IPI") { valorIPI },
            CampoNumber("Alíq. ICMS") { icmsAliq },
            CampoNumber("Alíq. IPI") { ipiAliq },
                )

  fun grava(listaNotas: List<NotaSaida>): ByteArray {
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
        listaNotas.flatMap { it.listaProdutos() }.sortedBy { it.loja }.forEach { produto ->
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