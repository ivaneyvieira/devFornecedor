package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.format
import java.time.LocalDate

open class Campo<T : Any, B>(val header: String, val produceValue: (B) -> T)

class CampoString<B>(header: String, produceValue: B.() -> String? = { "" }) : Campo<String, B>(header, produceValue={bean ->
  produceValue(bean) ?: ""
})

class CampoNumber<B>(header: String, produceValue: B.() -> Double? = { 0.00 }) : Campo<Double, B>(header, produceValue={bean ->
  produceValue(bean) ?: 0.00
})

class CampoInt<B>(header: String, produceValue: B.() -> Int? = { 0 }) : Campo<Int, B>(header, produceValue={bean ->
  produceValue(bean) ?: 0
})


class CampoDate<B>(header: String, produceValue: B.() -> LocalDate? = { null }) : Campo<String, B>(header, produceValue={bean ->
  produceValue(bean).format()
})