package br.com.astrosoft.devolucao.model.knfe

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.xml.parsers.DocumentBuilderFactory

fun Element.getElements(tagName: String): List<Element> {
  val elements = mutableListOf<Element>()
  val nodes = getElementsByTagName(tagName)
  for (i in 0 until nodes.length) {
    val element = nodes.item(i) as? Element
    if (element != null) elements.add(element)
  }
  return elements.toList()
}

fun Element.getElement(tagName: Regex): Element? {
  val nodeList = getElementsByTagName("*")
  for (i in 0 until nodeList.length) {
    val element = nodeList.item(i) as? Element
    if (element != null && tagName.matches(element.tagName)) return element
  }
  return null
}

fun Document.getElement(tagName: String): Element? {
  val nodeList = getElementsByTagName(tagName)
  return if (nodeList.length > 0) nodeList.item(0) as Element else null
}

fun Element.getElement(tagName: String): Element? {
  val nodeList = getElementsByTagName(tagName)
  return if (nodeList.length > 0) nodeList.item(0) as Element else null
}

fun Element?.getTextContent(tagName: String): String {
  return this?.getElement(tagName)?.textContent?.trim() ?: ""
}

fun Element?.getDoubleContent(tagName: String): Double {
  val text = this?.getElement(tagName)?.textContent?.trim() ?: return 0.0
  return text.toDoubleOrNull() ?: 0.0
}

fun Element?.getDateContent(tagName: String): LocalDateTime? {
  return try {
    val text = this?.getElement(tagName)?.textContent?.trim() ?: return null
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val zonedDateTime = ZonedDateTime.parse(text, formatter)
    zonedDateTime.toLocalDateTime()
  } catch (ex: DateTimeParseException) {
    null
  }
}


fun parseNotaFiscal(xmlString: String?): NFe? {
  try {
    xmlString ?: return null
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val inputSource = InputSource(xmlString.reader())
    val doc = builder.parse(inputSource)

    fun parseEndereco(element: Element?): Endereco? {
      element ?: return null
      return Endereco(
        xLgr = element.getTextContent("xLgr"),
        nro = element.getTextContent("nro"),
        xBairro = element.getTextContent("xBairro"),
        cMun = element.getTextContent("cMun"),
        xMun = element.getTextContent("xMun"),
        uf = element.getTextContent("UF"),
        cpf = element.getTextContent("CEP"),
        cPais = element.getTextContent("cPais"),
        xPais = element.getTextContent("xPais"),
        fone = element.getTextContent("fone")
      )
    }

    fun parseProduto(element: Element?): Produto? {
      element ?: return null
      return Produto(
        cProd = element.getTextContent("cProd"),
        cEAN = element.getTextContent("cEAN"),
        xProd = element.getTextContent("xProd"),
        ncm = element.getTextContent("NCM"),
        cfop = element.getTextContent("CFOP"),
        uCom = element.getTextContent("uCom"),
        qCom = element.getDoubleContent("qCom"),
        vUnCom = element.getDoubleContent("vUnCom"),
        vProd = element.getDoubleContent("vProd"),
        cEANTrib = element.getTextContent("cEANTrib"),
        uTrib = element.getTextContent("uTrib"),
        qTrib = element.getDoubleContent("qTrib"),
        vUnTrib = element.getDoubleContent("vUnTrib"),
        indTot = element.getTextContent("indTot"),
        xPed = element.getTextContent("xPed"),
        nItemPed = element.getTextContent("nItemPed")
      )
    }

    fun parseImposto(impostoElement: Element?): Imposto? {
      impostoElement ?: return null
      fun parseICMS(icmsElement: Element?): ICMS? {
        icmsElement ?: return null
        return ICMS(
          orig = icmsElement.getTextContent("orig"),
          cst = icmsElement.getTextContent("CST"),
          modBC = icmsElement.getTextContent("modBC"),
          mvaST = icmsElement.getTextContent("pMVAST").toDoubleOrNull() ?: 0.0,
          vBC = icmsElement.getTextContent("vBC").toDoubleOrNull() ?: 0.0,
          pICMS = icmsElement.getTextContent("pICMS").toDoubleOrNull() ?: 0.0,
          vICMS = icmsElement.getTextContent("vICMS").toDoubleOrNull() ?: 0.0,
          vBCST = icmsElement.getTextContent("vBCST").toDoubleOrNull() ?: 0.0,
          pICMSST = icmsElement.getTextContent("pICMSST").toDoubleOrNull() ?: 0.0,
          vICMSST = icmsElement.getTextContent("vICMSST").toDoubleOrNull() ?: 0.0,
        )
      }

      fun parseIPI(ipiElement: Element?): IPI? {
        ipiElement ?: return null
        return IPI(
          cEnq = ipiElement.getTextContent("cEnq"),
          tributado = ipiElement.getTextContent("CST") != "99",
          vBC = ipiElement.getTextContent("vBC").toDoubleOrNull() ?: 0.0,
          pIPI = ipiElement.getTextContent("pIPI").toDoubleOrNull() ?: 0.0,
          vIPI = ipiElement.getTextContent("vIPI").toDoubleOrNull() ?: 0.0
        )
      }

      fun parsePIS(pisElement: Element?): PIS? {
        pisElement ?: return null
        return PIS(
          cst = pisElement.getTextContent("CST"),
          vBC = pisElement.getTextContent("vBC").toDoubleOrNull() ?: 0.0,
          pPIS = pisElement.getTextContent("pPIS").toDoubleOrNull() ?: 0.0,
          vPIS = pisElement.getTextContent("vPIS").toDoubleOrNull() ?: 0.0
        )
      }

      fun parseCOFINS(cofinsElement: Element?): COFINS? {
        cofinsElement ?: return null
        return COFINS(
          cst = cofinsElement.getTextContent("CST"),
          vBC = cofinsElement.getTextContent("vBC").toDoubleOrNull() ?: 0.0,
          pCOFINS = cofinsElement.getTextContent("pCOFINS").toDoubleOrNull() ?: 0.0,
          vCOFINS = cofinsElement.getTextContent("vCOFINS").toDoubleOrNull() ?: 0.0
        )
      }

      val icmsElement = impostoElement.getElement("ICMS.+".toRegex())
      val ipiElement = impostoElement.getElement("IPITrib")
      val pisElement = impostoElement.getElement("PISAliq")
      val cofinsElement = impostoElement.getElement("COFINSAliq")

      return Imposto(
        icms = parseICMS(icmsElement),
        ipi = parseIPI(ipiElement),
        pis = parsePIS(pisElement),
        cofins = parseCOFINS(cofinsElement)
      )
    }

    fun parseDetalhe(element: Element): Detalhe {
      return Detalhe(
        nItem = element.getAttribute("nItem").toIntOrNull() ?: 0,
        prod = parseProduto(element.getElement("prod")),
        imposto = parseImposto(element.getElement("imposto"))
      )
    }

    val nfeElement = doc.getElement("NFe")
    val infNFeElement = nfeElement?.getElement("infNFe")
    val id : String = infNFeElement?.getAttribute("Id") ?: ""
    val versao = infNFeElement?.getAttribute("versao") ?: ""
    val ideElement = infNFeElement?.getElement("ide")
    val emitElement = infNFeElement?.getElement("emit")
    val destElement = infNFeElement?.getElement("dest")
    val detalhesElement = infNFeElement?.getElements("det").orEmpty()

    val ide = Ide(
      cUF = ideElement.getTextContent("cUF"),
      cNF = ideElement.getTextContent("cNF"),
      natOp = ideElement.getTextContent("natOp"),
      mod = ideElement.getTextContent("mod"),
      serie = ideElement.getTextContent("serie"),
      nNF = ideElement.getTextContent("nNF"),
      dhEmi = ideElement.getDateContent("dhEmi"),
      dhSaiEnt = ideElement.getDateContent("dhSaiEnt"),
      tpNF = ideElement.getTextContent("tpNF"),
      idDest = ideElement.getTextContent("idDest"),
      cMunFG = ideElement.getTextContent("cMunFG"),
      tpImp = ideElement.getTextContent("tpImp"),
      tpEmis = ideElement.getTextContent("tpEmis"),
      cDV = ideElement.getTextContent("cDV"),
      tpAmb = ideElement.getTextContent("tpAmb"),
      finNFe = ideElement.getTextContent("finNFe"),
      indFinal = ideElement.getTextContent("indFinal"),
      indPres = ideElement.getTextContent("indPres"),
      procEmi = ideElement.getTextContent("procEmi"),
      verProc = ideElement.getTextContent("verProc")
    )

    val emitente = Emitente(
      cnpj = emitElement.getTextContent("CNPJ"),
      xNome = emitElement.getTextContent("xNome"),
      xFant = emitElement.getTextContent("xFant"),
      enderEmit = parseEndereco(emitElement?.getElement("enderEmit")),
      ie = emitElement.getTextContent("IE"),
      crt = emitElement.getTextContent("CRT")
    )

    val destinatario = Destinatario(
      cnpj = destElement.getTextContent("CNPJ"),
      xNome = destElement.getTextContent("xNome"),
      enderDest = parseEndereco(destElement?.getElement("enderDest") as Element),
      indIEDest = destElement.getTextContent("indIEDest"),
      ie = destElement.getTextContent("IE")
    )

    val detalhes = detalhesElement.map { element ->
      parseDetalhe(element)
    }

    val totalElement = infNFeElement.getElement("total")
    val icmsTotElement = totalElement?.getElement("ICMSTot")
    val total = Total(
      icmsTot = ICMSTot(
        vBC = icmsTotElement.getDoubleContent("vBC"),
        vICMS = icmsTotElement.getDoubleContent("vICMS"),
        vProd = icmsTotElement.getDoubleContent("vProd"),
        vNF = icmsTotElement.getDoubleContent("vNF")
      )
    )

    return NFe(InfNFe(id = id, versao = versao, ide = ide, emit = emitente, dest = destinatario, detalhes = detalhes, total = total))
  } catch (e: Exception) {
    return null
  }
}
