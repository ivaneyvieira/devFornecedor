package br.com.astrosoft.devolucao.model.beans

import io.github.rushuat.ocell.annotation.FieldName
import java.time.LocalDate

class FornecedorNotaExcel(
  @FieldName("Loja")
  val loja: Int,
  @FieldName("NI")
  val ni: Int,
  @FieldName("NF")
  val nf: String?,
  @FieldName("Emissão")
  val emissao: String,
  @FieldName("Entrada")
  val entrada: String,
  @FieldName("Vencimento")
  val vencimento: String,
  @FieldName("Valor Nota")
  val valorNota: Double?,
  @FieldName("Observação")
  val obs: String?,
  @FieldName("Situação")
  val situacao: String?,
  @FieldName("Observação Parcela")
  val obsParcela: String?,
                         )