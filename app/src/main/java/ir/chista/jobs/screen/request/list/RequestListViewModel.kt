package ir.chista.jobs.screen.request.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.emptyID
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.Event
import ir.chista.util.Observables.toLiveData
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
