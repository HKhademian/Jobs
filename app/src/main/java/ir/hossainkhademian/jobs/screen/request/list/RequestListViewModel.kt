package ir.hossainkhademian.jobs.screen.request.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.util.Observables.toLiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.launch

internal class RequestListViewModel : ViewModel() {
  val requests = Repository.Requests.list().toLiveData()
  val selectedUserId: LiveData<ID> = MutableLiveData()
  val error: LiveData<Throwable> = MutableLiveData()
  val isRefreshing: LiveData<Boolean> = MutableLiveData()

  fun refresh() {
    error as MutableLiveData
    isRefreshing as MutableLiveData

    isRefreshing.postValue(true)
    launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      isRefreshing.postValue(false)
      error.postValue(ex)
    }) {
      Repository.Requests.refresh()
    }.invokeOnCompletion {
      isRefreshing.postValue(false)
    }
  }
}
