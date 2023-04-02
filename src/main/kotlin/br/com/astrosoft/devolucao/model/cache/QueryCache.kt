package br.com.astrosoft.devolucao.model.cache

import kotlinx.coroutines.*

class QueryCache<T : Any>(val fetch: () -> List<T>) {
  private var objects: List<T>? = null

  @OptIn(DelicateCoroutinesApi::class)
  fun updateFromDatabase() {
    GlobalScope.launch(Dispatchers.IO) {
      val updatedObjects = fetch()
      withContext(Dispatchers.Main) {
        objects = updatedObjects
      }
    }
  }

  fun objects(): List<T> {
    if (objects == null) {
      objects = fetch()
    }
    return objects.orEmpty()
  }
}
