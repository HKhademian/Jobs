package ir.hossainkhademian.jobs.screen.request.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.letObserveOn
import ir.hossainkhademian.util.ViewModels.getViewModel
import kotlinx.android.synthetic.main.activity_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail.view.*

class RequestDetailFragment : BaseFragment() {
  companion object {
    const val ARG_REQUEST_ID = "request.id"
    const val ARG_REQUEST_TITLE = "request.title"
  }

  private lateinit var viewModel: RequestDetailViewModel
  private lateinit var rootView: View
  private val requestTypeWorkerView get() = rootView.requestTypeWorkerView
  private val requestTypeCompanyView get() = rootView.requestTypeCompanyView
  private val requestView get() = rootView.requestView
  private val skillsView get() = rootView.skillsView
  private val userView get() = rootView.userView
  private val detailView get() = rootView.detailView
  private val brokersView get() = rootView.brokersView
  private val matchesView get() = rootView.matchesView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val requestId = arguments?.getString(ARG_REQUEST_ID) ?: ""
    val title = arguments?.getString(ARG_REQUEST_TITLE) ?: ""

    viewModel = getViewModel { RequestDetailViewModel(app, requestId) }

    activity?.toolbar?.title = title
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_request_detail, container, false)

    viewModel.request.letObserveOn(this, EmptyRequest, ::setRequest)

    return rootView
  }

  private fun setRequest(request: Request) {
    requestTypeWorkerView.isEnabled = request.type == RequestType.WORKER
    requestTypeCompanyView.isEnabled = request.type == RequestType.COMPANY
    requestView.request = request.job
    userView.user = request.user
    detailView.setText(request.detail)
    brokersView.setText(request.brokers.joinToString("\n") { it.title })
    matchesView.setText(request.matches.joinToString("\n") { it.toString() })

    skillsView.setTags(request.skills.map { it.title })
  }
}
