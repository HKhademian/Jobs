package ir.chista.jobs.screen.dashboard.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.chista.jobs.R
import ir.chista.jobs.util.BaseFragment
import ir.chista.util.ViewModels.getViewModel
import ir.chista.util.LiveDatas.observe
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment() {
  private lateinit var viewModel: HomeViewModel

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    viewModel = getViewModel { HomeViewModel() }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.fragment_home, container, false)

    viewModel.user.observe(this) {
      rootView.userView.user = it
    }

    return rootView
  }
}
