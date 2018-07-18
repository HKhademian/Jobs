package ir.hossainkhademian.jobs.screen.request.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.emptyID
import ir.hossainkhademian.jobs.util.BaseViewModel
import ir.hossainkhademian.util.Event
import ir.hossainkhademian.util.Observables.toLiveData
import kotlinx.coroutines.experimental.*

internal class RequestListViewModel : BaseViewModel() {
  val requests = Repository.Requests.list().toLiveData()
  val selectedId: LiveData<ID> = MutableLiveData()
  val isRefreshing: LiveData<Boolean> = MutableLiveData()

  fun init(selectedId: ID = emptyID) {
    this.selectedId as MutableLiveData

    this.selectedId.postValue(selectedId)
  }

  private var job: Job? = null
  fun refresh() {
    error as MutableLiveData
    isRefreshing as MutableLiveData

    job?.cancel()

    isRefreshing.postValue(true)
    job = launch(CommonPool + CoroutineExceptionHandler { _, ex ->
      isRefreshing.postValue(false)
      error.postValue(Event(ex))
      job = null
    }) {
      Repository.Requests.refresh()
      isRefreshing.postValue(false)
    }
  }

  fun postSelectedId(id: ID) {
    (selectedId as MutableLiveData).postValue(id)
  }
}
