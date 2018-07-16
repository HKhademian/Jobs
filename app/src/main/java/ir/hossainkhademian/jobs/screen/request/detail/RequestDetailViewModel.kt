package ir.hossainkhademian.jobs.screen.request.detail

import android.arch.lifecycle.AndroidViewModel
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.util.Observables.toLiveData

internal class RequestDetailViewModel(
  val app: App,
  val fragment: RequestDetailFragment,
  val requestId: ID
) : AndroidViewModel(app) {
  val request = Repository.requestsObservable
    .take(1)
    .findById(requestId)
    .toLiveData()

  fun edit() {
    fragment.onEditListener(requestId)
  }

  fun cancel() {
    fragment.onCancelListener(requestId)
  }

  fun sendChat(userId: ID) {
    fragment.onSendChatListener(userId)
  }
}
