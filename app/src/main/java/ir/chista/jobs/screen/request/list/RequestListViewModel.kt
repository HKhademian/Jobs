package ir.chista.jobs.screen.request.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ir.chista.jobs.data.DataManager
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.ID
import ir.chista.jobs.data.model.LocalRequest
import ir.chista.jobs.data.model.Request
import ir.chista.jobs.data.model.emptyID
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.Event
import ir.chista.util.Observables.toLiveData
import ir.chista.util.LiveDatas.map
import kotlinx.coroutines.experimental.*

internal class RequestListViewModel : BaseViewModel() {
  val dataMode = DataManager.modeObservable.toLiveData()
  val requests: LiveData<List<LocalRequest>> = Repository.Requests.list().toLiveData()
  val selectedId: LiveData<ID> = MutableLiveData()
  val isRefreshing: LiveData<Boolean> = MutableLiveData()
  val isEditable: LiveData<Boolean> = dataMode.map { mode -> mode == DataManager.Mode.Online }

  fun init(selectedId: ID = emptyID) {
    this.selectedId as MutableLiveData

    this.selectedId.postValue(selectedId)
  }

  private var job: Job? = null
  fun refresh() {
    error as MutableLiveData
    isRefreshing as MutableLiveData

    if (dataMode.value != DataManager.Mode.Online) {
      isRefreshing.postValue(false)
      postError(RuntimeException("You are in offline mode"))
      return
    }

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
