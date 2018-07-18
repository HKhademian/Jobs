package ir.hossainkhademian.jobs.screen.request.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.jobs.util.BaseViewModel
import ir.hossainkhademian.util.Event
import ir.hossainkhademian.util.Observables.toLiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.DisposableHandle
import kotlinx.coroutines.experimental.launch

internal class RequestListViewModel : BaseViewModel() {
  val requests = Repository.Requests.list().toLiveData()
  val selectedId: LiveData<ID> = MutableLiveData()
  val isRefreshing: LiveData<Boolean> = MutableLiveData()

  fun init(selectedId: ID = emptyID) {
    this.selectedId as MutableLiveData

    this.selectedId.postValue(selectedId)
  }

  private var disposable: DisposableHandle? = null
  fun refresh() {
    error as MutableLiveData
    isRefreshing as MutableLiveData

    disposable?.dispose()

    isRefreshing.postValue(true)
    disposable = launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      isRefreshing.postValue(false)
      error.postValue(Event(ex))
    }) {
      Repository.Requests.refresh()
    }.invokeOnCompletion {
      isRefreshing.postValue(false)
    }
  }

  fun postSelectedId(id: ID) {
    (selectedId as MutableLiveData).postValue(id)
  }
}
