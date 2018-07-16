package ir.hossainkhademian.util

import android.arch.lifecycle.*

object LiveDatas {
  fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> =
    Transformations.map(this, mapper)

  fun <T> LiveData<T>.mapSuccess(): LiveData<Result<T>> =
    map(Result.Companion::success)


  fun <T> merge(vararg liveData: LiveData<T>): LiveData<T> =
    liveData.foldRight(MediatorLiveData<T>()) { it, mediator ->
      mediator.apply {
        addSource(it) { value -> mediator.value = value }
      }
    }


  inline fun <T> LiveData<T?>.letObserveOn(owner: LifecycleOwner, crossinline onChange: (T) -> Unit) =
    observe(owner, Observer { it?.let { onChange(it) } })

  inline fun <T> LiveData<T?>.letObserveOn(owner: LifecycleOwner, default: T, crossinline onChange: (T) -> Unit) =
    observe(owner, Observer { onChange(it ?: default) })


  inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline onChange: (T) -> Unit) =
    observe(owner, Observer { it?.let { onChange(it) } })

  inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, default: T, crossinline onChange: (T) -> Unit) =
    observe(owner, Observer { onChange(it ?: default) })


  inline fun <T> LiveData<T?>.observeOn(owner: LifecycleOwner, crossinline onChange: (T?) -> Unit) =
    observe(owner, Observer { t -> onChange(t) })

  inline fun <T> LiveData<T?>.withObserverObserveOn(owner: LifecycleOwner, crossinline onChange: (T?, Observer<T?>) -> Unit) =
    observe(owner, object : Observer<T?> {
      override fun onChanged(t: T?) =
        onChange(t, this)
    })


  fun <T> LiveData<T?>.singleObserve(owner: LifecycleOwner, observer: Observer<T?>) =
    withObserverObserveOn(owner) { t, baseObserver ->
      observer.onChanged(t)
      removeObserver(baseObserver)
    }

  inline fun <T> LiveData<T?>.singleObserve(owner: LifecycleOwner, crossinline onChange: (T?) -> Unit) =
    withObserverObserveOn(owner) { t, baseObserver ->
      onChange(t)
      removeObserver(baseObserver)
    }

  fun <T> LiveData<T>.distinct(): LiveData<T> {
    val mediatorLiveData: MediatorLiveData<T> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
      if (it != mediatorLiveData.value)
        mediatorLiveData.value = it
    }
    return mediatorLiveData
  }

  /** https://medium.com/@gauravgyal/combine-results-from-multiple-async-requests-90b6b45978f7 */
  fun <A, B> zip(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
      var lastA: A? = null
      var lastB: B? = null

      fun update() {
        val localLastA = lastA
        val localLastB = lastB
        if (localLastA != null && localLastB != null)
          this.value = Pair(localLastA, localLastB)
      }

      addSource(a) {
        lastA = it
        update()
      }
      addSource(b) {
        lastB = it
        update()
      }
    }
  }
}
