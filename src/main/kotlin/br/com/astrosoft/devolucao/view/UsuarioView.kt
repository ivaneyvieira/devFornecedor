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

  override fun columns() =
          listOf(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name, UserSaci::impressora.name)

  override fun createGrid() = GridCrud(UserSaci::class.java)

  override fun formCrud(operation: CrudOperation?,
                        domainObject: UserSaci?,
                        readOnly: Boolean,
                        binder: Binder<UserSaci>): Component {
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
              0    -> "Todas as lojas"
              else -> lojas.firstOrNull { loja ->
                loja.no == storeno
              }?.descricao ?: ""
            }
          }
          isAllowCustomValue = false
          binder.bind(this, UserSaci::storeno.name)
        }
        formLayout {
          h4("Devolução Série 1") {
            colspan = 2
          }

          checkBox("Pedido") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::pedido.name)
          }
          checkBox("Nota série 1") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::nota01.name)
          }
          checkBox("Nota série 1 Coleta") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::nota01Coleta.name)
          }
          checkBox("Remessa de Conserto") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::remessaConserto.name)
          }
          checkBox("Ajuste Garantia") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::ajusteGarantia.name)
          }
          checkBox("Financeiro") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaFinanceiro.name)
          }
        }
        formLayout {
          h4("Devolução Série 66") {
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
          h4("Agenda") {
            colspan = 2
          }
          checkBox("Pré-entrada") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::agendaNaoAgendada.name)
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
          h4("Recebimento") {
            colspan = 2
          }
          checkBox("Nota Pendente") {
            isReadOnly = readOnly
            binder.bind(this, UserSaci::notaPendente.name)
          }
        }
      }
    }
  }
}

