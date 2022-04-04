package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
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
            CampoInt("Código") { codigo },
            CampoString("Descrição") { descricao },
            CampoString("Grade") { grade },
            CampoString("NCM") { ncm },
            CampoString("CST") { cst },
            CampoString("CFOP") { cfop },
            CampoString("Unid") { un },
            CampoInt("Quant") { qtde },
            CampoNumber("V. Unit") { valorUnitario },
            CampoNumber("V. Total") { valorTotal },
            CampoNumber("B. Cálc. ICMS") { baseICMS },
            CampoNumber("MVA") { valorMVA },
            CampoNumber("B. Cálc. ST") { baseSt },

            CampoNumber("Valor ST") { valorST },
            CampoNumber("Valor ICMS") { valorICMS },
            CampoNumber("Valor IPI") { valorIPI },
            CampoNumber("Alíq. ICMS") { icmsAliq },
            CampoNumber("Alíq. IPI") { ipiAliq },
            CampoNumber("V. Total") { valorTotalGeral },
            CampoString("Chave") {
              val size = chaveUlt?.length ?: return@CampoString ""
              if (size > 6) chaveUlt.substring(0, 6) else ""
            },
            CampoString("Chave Sefaz") { chaveSefaz ?: "" },
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
