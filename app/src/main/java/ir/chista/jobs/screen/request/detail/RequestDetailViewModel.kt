package ir.chista.jobs.screen.request.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.chista.jobs.data.DataManager
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.*
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.Observables.toLiveData
import ir.chista.util.LiveDatas.map

internal class RequestDetailViewModel : BaseViewModel() {
  val dataMode = DataManager.modeObservable.toLiveData()
  val request: LiveData<LocalRequest> = MutableLiveData()
  val isEditable = dataMode.map { mode -> mode == DataManager.Mode.Online }

  var listener: RequestDetailListener? = null
  private var disposable: Disposable? = null

  var requestId: ID
    get() = request.value?.id ?: emptyID
    set(requestId) {
      disposable?.dispose()
      disposable = Repository.Requests.findById(requestId)
        .subscribe {
          (request as MutableLiveData).postValue(it)
        }
    }

  fun init(requestId: ID = emptyID) {
    this.requestId = requestId
  }

  fun edit() {
    listener?.onRequestDetailEdit(request.value?.id ?: emptyID)
  }

  fun sendChat(user: User) {
    listener?.onRequestDetailChat(request.value?.id ?: emptyID, user.id)
  }

  fun cancel() {
    listener?.onRequestDetailCloseDone(request.value?.id ?: emptyID)
  }
}
