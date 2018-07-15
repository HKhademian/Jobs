package ir.hossainkhademian.jobs.screen.request.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.EmptyRequest
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.inlineObserve
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
  private val requestTypeView get() = rootView.requestTypeView
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

    viewModel.request.inlineObserve(this) { it ->
      val request = it ?: EmptyRequest

      requestTypeView.text = request.type.key
      requestView.request = request
      skillsView.setText(request.skills.joinToString("\n") { it.title })
      userView.user = request.user
      detailView.setText(request.detail)
      //brokersView.setText(request.brokers.joinToString("\n") { it.title })
      //matchesView.setText(request.matches.joinToString("\n") { it.title })
    }

    return rootView
  }
}
