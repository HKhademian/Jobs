package ir.hossainkhademian.jobs.screen.request.list

import android.arch.lifecycle.ViewModel
import ir.hossainkhademian.jobs.data.Repository
import ir.hossainkhademian.util.Observables.toLiveData

internal class RequestListViewModel : ViewModel() {
  val requests = Repository.requestsObservable
    .map { requests -> requests.sortedBy { request -> request.time } }
    .toLiveData()
}
