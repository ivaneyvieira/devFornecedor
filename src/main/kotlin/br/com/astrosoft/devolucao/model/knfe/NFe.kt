package br.com.astrosoft.devolucao.model.knfe

data class NFe(
    val infNFe: InfNFe
)

data class InfNFe(
    val ide: Ide,
    val emit: Emitente,
    val dest: Destinatario,
    val produtos: List<Produto>,
    val total: Total
)

data class Ide(
    val cUF: String,
    val cNF: String,
    val natOp: String,
    val mod: String,
    val serie: String,
    val nNF: String,
    val dhEmi: String,
    val dhSaiEnt: String,
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
    val CNPJ: String,
    val xNome: String,
    val xFant: String,
    val enderEmit: Endereco,
    val IE: String,
    val CRT: String
)

data class Destinatario(
    val CNPJ: String,
    val xNome: String,
    val enderDest: Endereco,
    val indIEDest: String,
    val IE: String
)

data class Produto(
    val cProd: String,
    val cEAN: String,
    val xProd: String,
    val NCM: String,
    val CFOP: String,
    val uCom: String,
    val qCom: String,
    val vUnCom: String,
    val vProd: String,
    val cEANTrib: String,
    val uTrib: String,
    val qTrib: String,
    val vUnTrib: String,
    val indTot: String,
    val xPed: String,
    val nItemPed: String
)

data class Total(
    val ICMSTot: ICMSTot
)

data class ICMSTot(
    val vBC: String,
    val vICMS: String,
    val vProd: String,
    val vNF: String
)

data class Endereco(
    val xLgr: String,
    val nro: String,
    val xBairro: String,
    val cMun: String,
    val xMun: String,
    val UF: String,
    val CEP: String,
    val cPais: String,
    val xPais: String,
    val fone: String
)
