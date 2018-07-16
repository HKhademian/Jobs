package ir.hossainkhademian.jobs.data.api

import com.squareup.moshi.JsonAdapter
import ir.hossainkhademian.jobs.data.model.IdModel

class MockStore<T : IdModel, D : T>(
  private val prefKey: String,
  private val adapter: JsonAdapter<D>,
  private val toData: T.() -> D,
  init: MockStore<T, D>.() -> Unit = {}) {

  val items = mutableListOf<T>()
  val saveItems = mutableListOf<T>()

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

  fun update(item: T, save: Boolean = true) {
    saveItems.removeAll { it.id == item.id }
    items.removeAll { it.id == item.id }

    saveItems += item
    items += item

    if (save)
      MockApiStorage.pref.edit().putStringSet(prefKey,
        saveItems.map { it.toData() }.map { adapter.toJson(it)!! }.toSet()
      ).apply()
  }
}
