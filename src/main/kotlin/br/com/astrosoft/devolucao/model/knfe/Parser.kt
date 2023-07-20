package br.com.astrosoft.devolucao.model.knfe

import org.w3c.dom.Element
import org.xml.sax.InputSource
import javax.xml.parsers.DocumentBuilderFactory

fun parseNotaFiscal(xmlString: String): NFe {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val inputSource = InputSource(xmlString.reader())
    val doc = builder.parse(inputSource)

    fun getTextContent(node: Element, tagName: String): String {
        return node.getElementsByTagName(tagName).item(0)?.textContent?.trim() ?: ""
    }

    fun parseEndereco(element: Element): Endereco {
        return Endereco(
            getTextContent(element, "xLgr"),
            getTextContent(element, "nro"),
            getTextContent(element, "xBairro"),
            getTextContent(element, "cMun"),
            getTextContent(element, "xMun"),
            getTextContent(element, "UF"),
            getTextContent(element, "CEP"),
            getTextContent(element, "cPais"),
            getTextContent(element, "xPais"),
            getTextContent(element, "fone")
        )
    }

    fun parseProduto(element: Element): Produto {
        return Produto(
            getTextContent(element, "cProd"),
            getTextContent(element, "cEAN"),
            getTextContent(element, "xProd"),
            getTextContent(element, "NCM"),
            getTextContent(element, "CFOP"),
            getTextContent(element, "uCom"),
            getTextContent(element, "qCom"),
            getTextContent(element, "vUnCom"),
            getTextContent(element, "vProd"),
            getTextContent(element, "cEANTrib"),
            getTextContent(element, "uTrib"),
            getTextContent(element, "qTrib"),
            getTextContent(element, "vUnTrib"),
            getTextContent(element, "indTot"),
            getTextContent(element, "xPed"),
            getTextContent(element, "nItemPed")
        )
    }

    val nfeElement = doc.getElementsByTagName("NFe").item(0) as Element
    val infNFeElement = nfeElement.getElementsByTagName("infNFe").item(0) as Element
    val ideElement = infNFeElement.getElementsByTagName("ide").item(0) as Element
    val emitElement = infNFeElement.getElementsByTagName("emit").item(0) as Element
    val destElement = infNFeElement.getElementsByTagName("dest").item(0) as Element
    val produtosElement = infNFeElement.getElementsByTagName("prod")

    val ide = Ide(
        getTextContent(ideElement, "cUF"),
        getTextContent(ideElement, "cNF"),
        getTextContent(ideElement, "natOp"),
        getTextContent(ideElement, "mod"),
        getTextContent(ideElement, "serie"),
        getTextContent(ideElement, "nNF"),
        getTextContent(ideElement, "dhEmi"),
        getTextContent(ideElement, "dhSaiEnt"),
        getTextContent(ideElement, "tpNF"),
        getTextContent(ideElement, "idDest"),
        getTextContent(ideElement, "cMunFG"),
        getTextContent(ideElement, "tpImp"),
        getTextContent(ideElement, "tpEmis"),
        getTextContent(ideElement, "cDV"),
        getTextContent(ideElement, "tpAmb"),
        getTextContent(ideElement, "finNFe"),
        getTextContent(ideElement, "indFinal"),
        getTextContent(ideElement, "indPres"),
        getTextContent(ideElement, "procEmi"),
        getTextContent(ideElement, "verProc")
    )

    val emitente = Emitente(
        getTextContent(emitElement, "CNPJ"),
        getTextContent(emitElement, "xNome"),
        getTextContent(emitElement, "xFant"),
        parseEndereco(emitElement.getElementsByTagName("enderEmit").item(0) as Element),
        getTextContent(emitElement, "IE"),
        getTextContent(emitElement, "CRT")
    )

    val destinatario = Destinatario(
        getTextContent(destElement, "CNPJ"),
        getTextContent(destElement, "xNome"),
        parseEndereco(destElement.getElementsByTagName("enderDest").item(0) as Element),
        getTextContent(destElement, "indIEDest"),
        getTextContent(destElement, "IE")
    )

    val produtos = mutableListOf<Produto>()
    for (i in 0 until produtosElement.length) {
        val produtoElement = produtosElement.item(i) as Element
        produtos.add(parseProduto(produtoElement))
    }

    val totalElement = infNFeElement.getElementsByTagName("total").item(0) as Element
    val ICMSTotElement = totalElement.getElementsByTagName("ICMSTot").item(0) as Element
    val total = Total(
        ICMSTot(
            getTextContent(ICMSTotElement, "vBC"),
            getTextContent(ICMSTotElement, "vICMS"),
            getTextContent(ICMSTotElement, "vProd"),
            getTextContent(ICMSTotElement, "vNF")
        )
    )

    return NFe(InfNFe(ide, emitente, destinatario, produtos, total))
}
