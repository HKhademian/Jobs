package ir.chista.jobs.screen.request.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import ir.chista.jobs.data.AccountManager
import ir.chista.jobs.data.DataManager
import ir.chista.jobs.data.Repository
import ir.chista.jobs.data.model.*
import ir.chista.jobs.dialog.BrokerRemoveDialog
import ir.chista.jobs.dialog.BrokerSelectDialog
import ir.chista.jobs.util.BaseViewModel
import ir.chista.util.Observables.toLiveData
import ir.chista.util.LiveDatas.map
import ir.chista.util.LiveDatas.zip
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

internal class RequestDetailViewModel : BaseViewModel() {
  val isOnline: LiveData<Boolean> = DataManager.isOnline.toLiveData()
  val account = AccountManager.observable.toLiveData()

  val request: LiveData<LocalRequest> = MutableLiveData()
  val isEditable = isOnline.map { isOnline -> isOnline }
  val isBrokerEditable = zip(account, isOnline).map { (account, isOnline) -> account.isAdmin && isOnline }

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

  fun sendChat(userId: ID) {
    listener?.onRequestDetailChat(request.value?.id ?: emptyID, userId)
  }

  fun removeBroker(brokerId: ID) {
    if (brokerId.isEmpty) return
    if (isOnline.value != true) return
    if (AccountManager.user.isNotAdmin) return

    request as MutableLiveData

    postActivityTask { activity ->
      BrokerRemoveDialog.show(activity, {}, {

        launch(CommonPool + CoroutineExceptionHandler { _, ex -> postError(ex) }) {
          Repository.Requests.removeBroker(requestId, brokerId)

          requestId = requestId
        }

      })
    }
  }

  fun addBroker() {
    if (isOnline.value != true) return
    if (AccountManager.user.isNotAdmin) return

    request as MutableLiveData

    postActivityTask { activity ->
      BrokerSelectDialog.show(activity, request.value?.brokerIds ?: emptyList()) { user ->

        launch(CommonPool + CoroutineExceptionHandler { _, ex -> postError(ex) }) {
          Repository.Requests.addBroker(requestId, user.id)

          requestId = requestId
        }

      }
    }
  }

  fun cancel() {
    listener?.onRequestDetailCloseDone(request.value?.id ?: emptyID)
  }
}
