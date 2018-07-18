package ir.chista.util

import android.arch.lifecycle.*

typealias Quadruple<A, B, C, D> = Pair<Pair<A, B>, Pair<C, D>>

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
          this.value = localLastA to localLastB
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

  fun <A, B, C> zip(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>): LiveData<Triple<A, B, C>> {
    return MediatorLiveData<Triple<A, B, C>>().apply {
      var lastA: A? = null
      var lastB: B? = null
      var lastC: C? = null

      fun update() {
        val localLastA = lastA
        val localLastB = lastB
        val localLastC = lastC
        if (localLastA != null && localLastB != null && localLastC != null)
          this.value = Triple(localLastA, localLastB, localLastC)
      }

      addSource(a) {
        lastA = it
        update()
      }
      addSource(b) {
        lastB = it
        update()
      }
      addSource(c) {
        lastC = it
        update()
      }
    }
  }

  fun <A, B, C, D> zip(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>): LiveData<Quadruple<A, B, C, D>> {
    return MediatorLiveData<Quadruple<A, B, C, D>>().apply {
      var lastA: A? = null
      var lastB: B? = null
      var lastC: C? = null
      var lastD: D? = null

      fun update() {
        val localLastA = lastA
        val localLastB = lastB
        val localLastC = lastC
        val localLastD = lastD
        if (localLastA != null && localLastB != null && localLastC != null && localLastD != null)
          this.value = (localLastA to localLastB) to (localLastC to localLastD)
      }

      addSource(a) {
        lastA = it
        update()
      }
      addSource(b) {
        lastB = it
        update()
      }
      addSource(c) {
        lastC = it
        update()
      }
      addSource(d) {
        lastD = it
        update()
      }
    }
  }

  //data class Quadruple<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
}
