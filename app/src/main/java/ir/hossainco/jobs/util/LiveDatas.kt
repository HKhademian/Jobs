package ir.hossainkhademian.myarch.util

import android.arch.lifecycle.*

fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> =
	Transformations.map(this, mapper)

fun <T> LiveData<T>.mapSuccess(): LiveData<Result<T>> =
	map(Result.Companion::success)


fun <T> merge(vararg liveData: LiveData<T>): LiveData<T> =
	liveData.foldRight(MediatorLiveData<T>()) { it, mediator -> mediator.apply { addSource(it, { value -> mediator.value = value }) } }


inline fun <T> LiveData<T>.inlineObserve(owner: LifecycleOwner, crossinline onChange: (T?) -> Unit) =
	observe(owner, Observer<T> { t -> onChange(t) })

inline fun <T> LiveData<T>.inlineObserve(owner: LifecycleOwner, crossinline onChange: (T?, Observer<T>) -> Unit) =
	observe(owner, object : Observer<T> {
		override fun onChanged(t: T?) =
			onChange(t, this)
	})


fun <T> LiveData<T>.singleObserve(owner: LifecycleOwner, observer: Observer<T>) =
	inlineObserve(owner) { t, baseObserver ->
		observer.onChanged(t)
		removeObserver(baseObserver)
	}

inline fun <T> LiveData<T>.singleObserve(owner: LifecycleOwner, crossinline onChange: (T?) -> Unit) =
	inlineObserve(owner) { t, baseObserver ->
		onChange(t)
		removeObserver(baseObserver)
	}
