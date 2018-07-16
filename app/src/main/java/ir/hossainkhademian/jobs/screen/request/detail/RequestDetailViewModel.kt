package ir.hossainkhademian.jobs.screen.request.detail

import android.arch.lifecycle.AndroidViewModel
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.jobs.data.model.EmptyRequest
import ir.hossainkhademian.jobs.data.model.ID
import ir.hossainkhademian.jobs.data.model.Request
import ir.hossainkhademian.jobs.data.model.findById
import ir.hossainkhademian.util.Observables.toLiveData

internal class RequestDetailViewModel(val app: App, val requestId: ID) : AndroidViewModel(app) {
  val request = Repository.requestsObservable.findById(requestId).toLiveData()

  fun cancel(onCancelListener: (Request) -> Unit) {
    val request = this.request.value ?: EmptyRequest
    onCancelListener(request)
  }
}
