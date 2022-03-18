package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPendenteViewModel(view: IDevolucaoPendenteView) :
        DevolucaoAbstractViewModel<IDevolucaoPendenteView>(view) {

  val tabNotaPendenteBaseViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteBase
  }
  val tabNotaPendenteNotaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteNota
  }
  val tabNotaPendenteEmailViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteEmail
  }
  val tabNotaPendenteTransitoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteTransito
  }
  val tabNotaPendenteFabricaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteFabrica
  }
  val tabNotaPendenteCConcedidoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCConcedido
  }
  val tabNotaPendenteCAplicadoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCAplicado
  }
  val tabNotaPendenteCAguardarViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCAguardar
  }
  val tabNotaPendenteCContaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCConta
  }
  val tabNotaPendenteBonificadaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteBonificada
  }
  val tabNotaPendenteReposicaoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteReposicao
  }
  val tabNotaPendenteRetornoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteRetorno
  }

  override fun listTab() = listOf(
    view.tabNotaPendenteBase,
    view.tabNotaPendenteNota,
    view.tabNotaPendenteEmail,
    view.tabNotaPendenteTransito,
    view.tabNotaPendenteFabrica,
    view.tabNotaPendenteCAguardar,
    view.tabNotaPendenteCConcedido,
    view.tabNotaPendenteCAplicado,
    view.tabNotaPendenteCConta,
    view.tabNotaPendenteBonificada,
    view.tabNotaPendenteReposicao,
    view.tabNotaPendenteRetorno,
                                 )
}

interface IDevolucaoPendenteView : IDevolucaoAbstractView {
  val tabNotaPendenteBase: ITabNotaPendente
  val tabNotaPendenteNota: ITabNotaPendente
  val tabNotaPendenteEmail: ITabNotaPendente
  val tabNotaPendenteTransito: ITabNotaPendente
  val tabNotaPendenteFabrica: ITabNotaPendente
  val tabNotaPendenteCAguardar: ITabNotaPendente
  val tabNotaPendenteCConcedido: ITabNotaPendente
  val tabNotaPendenteCAplicado: ITabNotaPendente
  val tabNotaPendenteCConta: ITabNotaPendente
  val tabNotaPendenteBonificada: ITabNotaPendente
  val tabNotaPendenteReposicao: ITabNotaPendente
  val tabNotaPendenteRetorno: ITabNotaPendente
}

enum class ESituacaoPedido(val valueStr: String, val descricao: String) {
  VAZIO(valueStr = "", descricao = ""),
  EMAIL_ENVIADO(valueStr = "PED_EML_EVD", descricao = "E-mail Enviado"),
  NFD_AUTOZ(valueStr = "PED_NFD_ATZ", descricao = "NFD Autorizada"),
  AJUSTE_GARANTIA(valueStr = "PED_AJT_GAR", descricao = "Ajuste Garantia"),
  ASSISTENCIA(valueStr = "ASSISTENCIA", descricao = "Assistencia")
}

enum class ESituacaoPendencia(val title: String,
                              val valueStr: String?,
                              val descricao: String,
                              val userCol: String? = null,
                              val dataSitCol: String? = null,
                              val situacaoCol: String? = null,
                              val notaCol: String? = "",
                              val docCol: String? = null,
                              val numeroCol: String? = null,
                              val niCol: String? = null,
                              val dataCol: String? = null) {
  BASE(title = "Base", valueStr = null, descricao = "Base"),
  NOTA(title = "Nota",
       valueStr = "",
       descricao = "Nota",
       notaCol = "Aguadar",
       userCol = "",
       dataSitCol = "",
       docCol = "",
       numeroCol = "",
       niCol = "",
       dataCol = "Saída"),
  EMAIL(title = "E-mail", valueStr = "E-MAIL", descricao = "E-mail", docCol = "", numeroCol = "", niCol = ""),
  TRANSITO(title = "Trânsito",
           valueStr = "TRANSITO",
           descricao = "Trânsito",
           dataCol = "Saída",
           docCol = "",
           numeroCol = "",
           niCol = ""),
  FABRICA(title = "Fabrica", valueStr = "FABRICA", descricao = "Fábrica", docCol = "", numeroCol = "", niCol = ""),
  CREDITO_AGUARDAR(title = "Aguardar Crédito",
                   valueStr = "CREDITO_AGUARDAR",
                   descricao = "Aguardar Crédito",
                   docCol = "",
                   niCol = "",
                   numeroCol = "Tipo:L"),
  CREDITO_CONCEDIDO(title = "Crédito Concedido",
                    valueStr = "CREDITO_CONCEDIDO",
                    descricao = "Crédito Concedido",
                    dataCol = "Previsão",
                    docCol = "Título",
                    numeroCol = ""),
  CREDITO_APLICADO(title = "Crédito Aplicado",
                   valueStr = "CREDITO_APLICADO",
                   descricao = "Crédito Aplicado",
                   docCol = "",
                   numeroCol = "Título",
                   dataCol = "Vencimento"),
  CREDITO_CONTA(title = "Crédito Conta",
                valueStr = "CREDITO_CONTA",
                descricao = "Crédito Conta",
                docCol = "Ag",
                numeroCol = "CC",
                niCol = "Banco:L",
                dataCol = "Previsão"),
  BONIFICADA(title = "Bonificada",
             valueStr = "BONIFICADA",
             descricao = "Bonificado",
             docCol = "",
             numeroCol = "Nota",
             dataCol = "Emissão"),
  REPOSICAO(title = "Reposição",
            valueStr = "REPOSICAO",
            descricao = "Reposição",
            docCol = "",
            numeroCol = "Nota",
            dataCol = "Emissão"),
  RETORNO(title = "Retorno",
          valueStr = "RETORNO",
          descricao = "Retorno",
          docCol = "",
          numeroCol = "Nota",
          dataCol = "Emissão")
}