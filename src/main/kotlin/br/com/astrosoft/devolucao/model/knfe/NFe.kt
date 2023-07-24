package br.com.astrosoft.devolucao.model.knfe

import java.time.LocalDateTime

data class NFe(
  val infNFe: InfNFe
)

data class InfNFe(
  val id: String,
  val versao: String,
  val ide: Ide,
  val emit: Emitente,
  val dest: Destinatario,
  val detalhes: List<Detalhe>,
  val total: Total
)

data class Ide(
  val cUF: String,
  val cNF: String,
  val natOp: String,
  val mod: String,
  val serie: String,
  val nNF: String,
  val dhEmi: LocalDateTime?,
  val dhSaiEnt: LocalDateTime?,
  val tpNF: String,
  val idDest: String,
  val cMunFG: String,
  val tpImp: String,
  val tpEmis: String,
  val cDV: String,
  val tpAmb: String,
  val finNFe: String,
  val indFinal: String,
  val indPres: String,
  val procEmi: String,
  val verProc: String
)

data class Emitente(
  val cnpj: String,
  val xNome: String,
  val xFant: String,
  val enderEmit: Endereco?,
  val ie: String,
  val crt: String
)

data class Destinatario(
  val cnpj: String,
  val xNome: String,
  val enderDest: Endereco?,
  val indIEDest: String,
  val ie: String
)

data class Detalhe(
  val nItem: Int,
  val prod: Produto?,
  val imposto: Imposto?
)

data class Produto(
  val cProd: String,
  val cEAN: String,
  val xProd: String,
  val ncm: String,
  val cfop: String,
  val uCom: String,
  val qCom: Double,
  val vUnCom: Double,
  val vProd: Double,
  val cEANTrib: String,
  val uTrib: String,
  val qTrib: Double,
  val vUnTrib: Double,
  val indTot: String,
  val xPed: String,
  val nItemPed: String
)

data class Imposto(
  val icms: ICMS?,
  val ipi: IPI?,
  val pis: PIS?,
  val cofins: COFINS?
)

data class ICMS(
  val orig: String,
  val cst: String,
  val modBC: String,
  val mvaST: Double,
  val vBC: Double,
  val pICMS: Double,
  val vICMS: Double
)

data class IPI(
  val cEnq: String,
  val tributado: Boolean,
  val vBC: Double,
  val pIPI: Double,
  val vIPI: Double
)

data class PIS(
  val cst: String,
  val vBC: Double,
  val pPIS: Double,
  val vPIS: Double
)

data class COFINS(
  val cst: String,
  val vBC: Double,
  val pCOFINS: Double,
  val vCOFINS: Double
)

data class Total(
  val icmsTot: ICMSTot
)

data class ICMSTot(
  val vBC: Double,
  val vICMS: Double,
  val vProd: Double,
  val vNF: Double
)

data class Endereco(
  val xLgr: String,
  val nro: String,
  val xBairro: String,
  val cMun: String,
  val xMun: String,
  val uf: String,
  val cpf: String,
  val cPais: String,
  val xPais: String,
  val fone: String
)
