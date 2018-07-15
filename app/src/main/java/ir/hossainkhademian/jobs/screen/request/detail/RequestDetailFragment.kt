package ir.hossainkhademian.jobs.screen.request.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.ViewModels.getViewModel
import kotlinx.android.synthetic.main.activity_chat_detail.*

class RequestDetailFragment : BaseFragment() {
  companion object {
    const val ARG_REQUEST_ID = "request.id"
    const val ARG_REQUEST_TITLE = "request.title"
  }

  private lateinit var viewModel: RequestDetailViewModel
  private lateinit var rootView: View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val requestId = arguments?.getString(ARG_REQUEST_ID) ?: ""
    val title = arguments?.getString(ARG_REQUEST_TITLE) ?: ""

    viewModel = getViewModel { RequestDetailViewModel(app, requestId) }

    activity?.toolbar?.title = title
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_chat_detail, container, false)



    return rootView
  }
}
