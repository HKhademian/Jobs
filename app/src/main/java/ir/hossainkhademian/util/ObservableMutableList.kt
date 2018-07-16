package ir.hossainkhademian.util

import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class ObservableMutableList<T>(
  private val subject: Subject<List<T>> = BehaviorSubject.create<List<T>>(),
  private val list: MutableList<T> = mutableListOf(),
  init: ObservableMutableList<T>.() -> Unit = {}
) : AbstractMutableList<T>() {
  val observable = subject.hide()

  init {
    init()
    update()
  }

  fun update() = update {}
  fun <T> update(block: () -> T): T {
    val res = block.invoke()
    try {
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
    list.addAll(elements)
  }

  operator fun component1() = observable
  operator fun component2() = this
}
