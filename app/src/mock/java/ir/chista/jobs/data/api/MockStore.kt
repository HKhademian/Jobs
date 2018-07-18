package ir.chista.jobs.data.api

import com.squareup.moshi.JsonAdapter
import ir.chista.jobs.data.model.IdModel

class MockStore<M : IdModel>(
  private val prefKey: String,
  private val adapter: JsonAdapter<M>,
  init: MockStore<M>.() -> Unit = {}) {

  val items = mutableListOf<M>()
  val saveItems = mutableListOf<M>()

  init {
    init()
  }

  fun load() {
    saveItems.clear()
    MockApiStorage.pref
      .getStringSet(prefKey, emptySet())
      .map { adapter.fromJson(it)!! }
      .forEach { update(it, false) }
  }

  fun update(item: M, save: Boolean = true) {
    saveItems.removeAll { it.id == item.id }
    items.removeAll { it.id == item.id }

    saveItems += item
    items += item

    if (save)
      MockApiStorage.pref.edit().putStringSet(prefKey,
        saveItems.map { it }.map { adapter.toJson(it)!! }.toSet()
      ).apply()
  }
}
