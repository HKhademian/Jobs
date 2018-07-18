package ir.chista.jobs.util

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ir.chista.util.Event


open class BaseViewModel : ViewModel() {
  private val _error = MutableLiveData<Event<Throwable>>()
  val error: LiveData<Event<Throwable>> = _error

  private val _activity = MutableLiveData<Event<(Activity) -> Unit>>()
  val activity: LiveData<Event<(Activity) -> Unit>> = _activity


  protected fun postError(ex: Throwable) =
    _error.postValue(Event(ex))

  protected fun postActivityTask(task: (Activity) -> Unit) =
    _activity.postValue(Event(task))
}
