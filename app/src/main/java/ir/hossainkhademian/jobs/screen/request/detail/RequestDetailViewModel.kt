package ir.hossainkhademian.jobs.screen.request.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.util.BaseViewModel
import ir.hossainkhademian.util.Event

internal class RequestDetailViewModel : BaseViewModel() {
  val request: LiveData<LocalRequest> = MutableLiveData()
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

  fun init() {
    requestId = emptyID
  }

  fun edit() {
    listener?.onRequestDetailEdit(request.value ?: EmptyRequest)
  }

  fun sendChat(user: User) {
    listener?.onRequestDetailChat(request.value ?: EmptyRequest, user)
  }

  fun cancel() {
    listener?.onRequestDetailCancel(request.value ?: EmptyRequest)
  }
}
