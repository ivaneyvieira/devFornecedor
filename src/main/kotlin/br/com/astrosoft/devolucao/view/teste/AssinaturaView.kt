package br.com.astrosoft.devolucao.view.teste

import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.teste.AssinaturaViewModel
import br.com.astrosoft.devolucao.viewmodel.teste.IAssinaturaView
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import de.f0rce.signaturepad.SignaturePad

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Assinatura")
@CssImport("./styles/gridTotal.css")
class AssinaturaView : ViewLayout<AssinaturaViewModel>(), IAssinaturaView {
  override val viewModel: AssinaturaViewModel
    get() = AssinaturaViewModel(this)

  override fun isAccept(user: IUser): Boolean {
    return Config.user?.admin == true
  }

  init {
    verticalLayout {
      h1("Contrato")
      p {
        this.isExpand = true
        this.text =
          """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque non odio sit amet velit laoreet aliquet. Quisque sed arcu eros. Quisque ultrices neque vel risus accumsan, sed iaculis massa feugiat. Cras imperdiet felis sit amet tortor vulputate porttitor. Curabitur eu sem sit amet nunc ornare blandit. Etiam id elit quis sem aliquet dapibus at et libero. Donec varius efficitur odio in convallis. Vestibulum sed risus in lacus elementum scelerisque eget a eros. Quisque vel venenatis diam. Vivamus tellus erat, bibendum et dapibus vitae, posuere ut mi. Curabitur facilisis lectus sit amet pretium vestibulum. Aliquam pulvinar imperdiet sem, et tincidunt purus posuere et. Sed faucibus arcu velit, vestibulum maximus neque iaculis sed.

Phasellus luctus, dui nec consequat mattis, nibh ligula elementum lacus, id dictum mauris arcu quis justo. Nam laoreet justo est. Vestibulum congue eleifend suscipit. Fusce placerat leo sed elit tincidunt, id egestas lacus aliquam. Sed vel dignissim metus, eu sagittis augue. Morbi tincidunt elementum urna, vel faucibus quam ullamcorper ac. Etiam vel viverra orci. Donec felis dolor, eleifend condimentum feugiat non, rhoncus eu orci. Nullam ornare, ex eget volutpat ornare, arcu quam tristique nunc, et dignissim lorem neque vel libero."""
      }
      val signature = SignaturePad()
      signature.height = "150px"
      signature.width = "100%"
      signature.setBackgroundColor(0, 0, 0, 0)
      signature.penColor = "#000000"
      button("Apagar") {
        onLeftClick {
          signature.clear()
        }
      }
      add(signature)
    }
  }
}