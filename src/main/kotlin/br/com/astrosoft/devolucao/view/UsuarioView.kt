package br.com.astrosoft.devolucao.view

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.UsuarioViewModel
import br.com.astrosoft.framework.view.UserLayout
import br.com.astrosoft.framework.viewmodel.IUsuarioView
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.crudui.crud.CrudOperation
import org.vaadin.crudui.crud.CrudOperation.*
import org.vaadin.crudui.crud.impl.GridCrud

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Usuário")
class UsuarioView : UserLayout<UserSaci, UsuarioViewModel>(), IUsuarioView {
  override val viewModel = UsuarioViewModel(this)
  override fun columns(): List<String> {
    return listOf(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name, UserSaci::impressora.name)
  }

  override fun createGrid() = GridCrud(UserSaci::class.java)

  override fun formCrud(
    operation: CrudOperation?,
    domainObject: UserSaci?,
    readOnly: Boolean,
    binder: Binder<UserSaci>
  ): Component {
    return FormLayout().apply {
      if (operation in listOf(READ, DELETE, UPDATE)) integerField("Número") {
        isReadOnly = readOnly
        binder.bind(this, UserSaci::no.name)
      }
      if (operation in listOf(ADD, READ, DELETE, UPDATE)) textField("Login") {
        isReadOnly = readOnly
        binder.bind(this, UserSaci::login.name)
      }
      if (operation in listOf(READ, DELETE, UPDATE)) textField("Nome") {
        isReadOnly = true
        binder.bind(this, UserSaci::name.name)
      }
      if (operation in listOf(ADD, READ, DELETE, UPDATE)) {
        comboBox<Int>("Número Loja") {
          isReadOnly = readOnly
          isAllowCustomValue = false
          val lojas = viewModel.allLojas()
          val values = lojas.map { it.no } + listOf(0)
          setItems(values.distinct().sorted())
          this.setItemLabelGenerator { storeno ->
            when (storeno) {
              0 -> "Todas as lojas"
              else -> lojas.firstOrNull { loja ->
                loja.no == storeno
              }?.descricao ?: ""
            }
          }
          isAllowCustomValue = false
          binder.bind(this, UserSaci::storeno.name)
        }
        passwordField("Senha Impressão") {
          isReadOnly = readOnly
          binder.bind(this, UserSaci::senhaPrint.name)
        }
        formLayout {
          h4("Pendentes") {
            colspan = 2
          }
          checkBox("Base") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteBASE.name)
          }
          checkBox("Nota") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteNOTA.name)
          }
          checkBox("E-mail") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteEMAIL.name)
          }
          checkBox("Trânsito") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteTRANSITO.name)
          }
          checkBox("Fábrica") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteFABRICA.name)
          }
          checkBox("Aguardar Crédito") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteCREDITO_AGUARDAR.name)
          }
          checkBox("Crédito Concedido") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteCREDITO_CONCEDIDO.name)
          }
          checkBox("Crédito Aplicado") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteCREDITO_APLICADO.name)
          }
          checkBox("Crédito Conta") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteCREDITO_CONTA.name)
          }
          checkBox("Bonificada") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteBONIFICADA.name)
          }
          checkBox("Reposição") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteREPOSICAO.name)
          }
          checkBox("Retorno") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteRETORNO.name)
          }
          checkBox("Coleta Pendente") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteAGUARDA_COLETA.name)
          }
          checkBox("Cte Pendente") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::forPendenteASSINA_CTE.name)
          }
        }
        formLayout {
          h4("NFD") {
            colspan = 2
          }
          checkBox("Nota série 1") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::nota01.name)
          }
          checkBox("Remessa de Conserto") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::remessaConserto.name)
          }
          checkBox("Financeiro") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaFinanceiro.name)
          }
          checkBox("Sap x Saci") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::conferenciaSap.name)
          }
          checkBox("Sap") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::sap.name)
          }
          checkBox("Desconto") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::desconto.name)
          }
        }
        formLayout {
          h4("Interna") {
            colspan = 2
          }
          checkBox("Base") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::ajusteGarantia.name)
          }
          checkBox("Pendente") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::ajusteGarantiaPendente.name)
          }
          checkBox("Pago") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::ajusteGarantiaPago.name)
          }
          checkBox("Perca") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::ajusteGarantiaPerca.name)
          }
          checkBox("Série 66") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::ajusteGarantia66.name)
          }
        }
        formLayout {
          h4("Pedido") {
            colspan = 2
          }

          checkBox("eDITOR") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::pedidoEditor.name)
          }
          checkBox("Pendente") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::pedidoPendente.name)
          }

          checkBox("Finalizado") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::pedidoFinalizado.name)
          }
        }
        formLayout {
          h4("Série 66") {
            colspan = 2
          }
          checkBox("Nota série 66") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::nota66.name)
          }
          checkBox("Nota série 66 pago") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::nota66Pago.name)
          }
          checkBox("Retorno 66") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entrada.name)
          }
          checkBox("Email Recebido") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::emailRecebido.name)
          }
        }
        formLayout {
          h4("Compra") {
            colspan = 2
          }
          checkBox("Pedidos") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::compraPedidos.name)
          }
          checkBox("Conferir") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::compraConferir.name)
          }
          checkBox("Conferido") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::compraConferido.name)
          }
        }
        formLayout {
          h4("Demanda") {
            colspan = 2
          }
          checkBox("Agenda") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::demandaAgenda.name)
          }
          checkBox("Concluído") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::demandaConcluido.name)
          }
        }
        formLayout {
          h4("Agenda") {
            colspan = 2
          }
          checkBox("Pré-entrada") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::agendaPreEntrada.name)
          }
          checkBox("Rastreamento") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::agendaRastreamento.name)
          }
          checkBox("Agendada") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::agendaAgendada.name)
          }
          checkBox("Recebida") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::agendaRecebida.name)
          }
        }
        formLayout {
          h4("Entrada") {
            colspan = 2
          }
          checkBox("NDD") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaNdd.name)
          }
          checkBox("Receber") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaNddReceber.name)
          }
          checkBox("Recebido") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaNddRecebido.name)
          }
          checkBox("NF x Prec Fiscal") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaNddNFPrec.name)
          }
          checkBox("NF x Prec Info") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaNddNFPrecInfo.name)
          }
          checkBox("Entrada") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaNddNFEntrada.name)
          }
          checkBox("Frete") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaFrete.name)
          }
          checkBox("Preço") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaPreco.name)
          }
          checkBox("Pre Recebimento") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaPrecoPreRec.name)
          }
          checkBox("Frete 2") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaFretePer.name)
          }
          checkBox("Cte") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaCte.name)
          }
          checkBox("XML") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::entradaFileNFE.name)
          }
        }
        formLayout {
          h4("Recebimento") {
            colspan = 2
          }
          checkBox("Nota Pendente") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaPendente.name)
          }
        }
        formLayout {
          h4("Saída") {
            colspan = 2
          }
          checkBox("NF Saída") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaSaida.name)
          }
        }
        formLayout {
          h4("Impressão de NF Saida") {
            colspan = 2
          }
          checkBox("Reimpressão") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaSaidaReimpressao.name)
          }
          checkBox("2ª Via") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaSaida2Via.name)
          }
          checkBox("Cópia") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaSaidaCopia.name)
          }
          checkBox("Editor Reimpressão") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::reimpressao.name)
          }
        }
      }
    }
  }
}


