package ir.hossainkhademian.util

import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class ObservableMutableList<T>(
  private val subject: Subject<List<T>> = BehaviorSubject.create<List<T>>(),
  private val list: MutableList<T> = mutableListOf(),
  init: ObservableMutableList<T>.() -> Unit = {}
) : AbstractMutableList<T>() {
  val observable = subject.hide()!!
  private var freeze: Boolean = false

  init {
    init()
    update()
  }

  fun update() = update {}
  fun <T> update(block: () -> T): T {
    val res = block.invoke()
    if (!freeze) try {
      subject.onNext(list)
    } catch (ex: Exception) {
      ex.printStackTrace()
      Log.e(ObservableMutableList::class.java.simpleName, ex.message)
    }
    return res
  }


  override val size get() = list.size
  override operator fun get(index: Int) = list[index]

  override fun add(index: Int, element: T) = update {
    list.add(index, element)
  }

  override fun removeAt(index: Int): T = update {
    list.removeAt(index)
  }

  override operator fun set(index: Int, element: T): T = update {
    list.set(index, element)
  }

  override fun clear() = update {
    list.clear()
  }

  fun replace(elements: Collection<T>) = update {
    list.clear()
    list += elements
  }

  operator fun component1() = observable
  operator fun component2() = this

  fun freeze(updateAtLast: Boolean = false, block: ObservableMutableList<T>.() -> Unit) = try {
    freeze = true
    block()
    freeze = false
    if (updateAtLast) update()
    Unit
  } finally {
    freeze = false
  }

  fun merge(item: T, updateAtLast: Boolean = true, predicate: (T) -> Boolean = { false }) = freeze(updateAtLast) {
    list.removeAll(predicate)
    list += item
  }

  fun merge(items: Iterable<T>, updateAtLast: Boolean = true, predicate: (T) -> Boolean = { false }) = freeze(updateAtLast) {
    list.removeAll(predicate)
    list += items
  }

  fun source(items: Iterable<T>, updateAtLast: Boolean = true) = freeze(updateAtLast) {
    list.clear()
    list += items
  }
}
