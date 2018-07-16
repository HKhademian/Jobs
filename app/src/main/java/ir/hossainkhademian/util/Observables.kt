package ir.hossainkhademian.util

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

object Observables {
  fun <T1, T2, R> Observable<T1>.withLatestFrom(second: ObservableSource<T2>, combiner: (T1, T2) -> R): Observable<R> =
    withLatestFrom(second, BiFunction { a, b -> combiner.invoke(a, b) })

  fun <T1, T2, R> combineLatest(first: ObservableSource<T1>, second: ObservableSource<T2>, combiner: (T1, T2) -> R): Observable<R> =
    Observable.combineLatest(first, second, BiFunction { a, b -> combiner.invoke(a, b) })

  fun <T1, T2, R> zip(first: ObservableSource<T1>, second: ObservableSource<T2>, combiner: (T1, T2) -> R): Observable<R> =
    Observable.zip(first, second, BiFunction { a, b -> combiner.invoke(a, b) })

  // fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)


  fun <T> Observable<T>.debounceAfter(count: Long = 1, timeout: Long, unit: TimeUnit) =
    take(count).concatWith(skip(count).debounce(timeout, unit))

  fun <T> Observable<T>.toLiveData() = object : LiveData<T>() {
    private var disposable: Disposable? = null

    override fun onActive() {
      super.onActive()
      disposable?.dispose()
      // disposable = observeOn(AndroidSchedulers.mainThread()).subscribe { value = it }
      disposable = subscribe(::postValue)
    }

    override fun onInactive() {
      super.onInactive()
      disposable?.dispose()
      disposable = null
    }
  }

//  fun Disposable.attachToLifecycle(owner: LifecycleOwner) {
//    owner.lifecycle.addObserver(LifecycleDisposable(this))
//  }
//
//  private class LifecycleDisposable(obj: Disposable) :   DefaultLifecycleObserver, Disposable by obj {
//    override fun onStop(owner: LifecycleOwner) {
//      if (!isDisposed) {
//        dispose()
//      }
//    }
//  }
}
